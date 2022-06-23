package com.daftech.sweetectapp.core.repository

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.daftech.sweetectapp.BuildConfig
import com.daftech.sweetectapp.core.data.DataHistory
import com.daftech.sweetectapp.core.data.FoodData
import com.daftech.sweetectapp.core.data.HistoryFirebase
import com.daftech.sweetectapp.core.data.signin.SignIn
import com.daftech.sweetectapp.core.data.signup.SignUp
import com.daftech.sweetectapp.core.source.FoodDataSource
import com.daftech.sweetectapp.core.source.LocalDataSource
import com.daftech.sweetectapp.ml.ModelRegulerizer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class FoodRepository private constructor(private val localDataSource: LocalDataSource): FoodDataSource {

    companion object{
        @Volatile
        private var instance: FoodRepository? = null

        fun getInstance(localData: LocalDataSource): FoodRepository =
            instance ?: synchronized(this){
                instance ?: FoodRepository(localData).apply { instance = this }
            }
    }

    override fun getAllFoods(): LiveData<List<FoodData>> {
        val foodsResult = MutableLiveData<List<FoodData>>()
        localDataSource.getFoods(object : LocalDataSource.LoadFoodsCallback{
            override fun onAllFoodsReceived(foodResponses: List<FoodData>) {
                val foodList = ArrayList<FoodData>()
                for ( response in foodResponses ){
                    val food = FoodData(
                        response.labels,
                        response.calorie,
                        response.sugar
                    )
                    foodList.add(food)
                }
                foodsResult.postValue(foodList)
            }
        })
        return foodsResult
    }

    override fun getTensorFoods(context: Context, bitmap: Bitmap): FloatArray {
        //implement Tflite
        val model = ModelRegulerizer.newInstance(context)

        val imageProcessor = ImageProcessor.Builder().add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR)).build()

        var tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(bitmap)
        tensorImage = imageProcessor.process(tensorImage)

        // Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
        val byteBuffer = tensorImage.buffer
        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        model.close()

        return outputFeature0.floatArray
    }

    override fun signInToFirebase(signIn: SignIn, context: Context): LiveData<SignIn> {
        val ref = FirebaseAuth.getInstance()
        val result = MutableLiveData<SignIn>()
        ref.signInWithEmailAndPassword(signIn.email!!, signIn.pass!!).addOnCompleteListener { task ->
            if (task.isSuccessful) {

                val getIn = SignIn(
                    signIn.email,
                    signIn.pass,
                    task.isSuccessful
                )

                result.postValue(getIn)
                Log.d("Isi Sukses", task.isSuccessful.toString())

            } else {
                Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT).show()
                Log.d("Isi Sukses", task.isSuccessful.toString())
            }
        }
        return result
    }

    override fun signUpToFirebase(signUp: SignUp, context: Context) {
        val signUpRef = FirebaseAuth.getInstance()

        val ref = FirebaseDatabase.getInstance().getReferenceFromUrl(BuildConfig.FIREBASE_ADDRESS)

        signUpRef.createUserWithEmailAndPassword(signUp.email!!, signUp.pass!!).addOnCompleteListener { authTask ->
            val noteFirebase = HistoryFirebase(
                id = signUpRef.uid,
                email = signUp.email,
                dataHistory = null
            )
            when{
                signUpRef.uid != null -> {
                    if (authTask.isSuccessful){
                        ref.child("Users").child(signUpRef.uid!!).setValue(noteFirebase).addOnCompleteListener {
                            if (it.isSuccessful){
                                Toast.makeText(context, "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
                            }else{
                                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }else{
                        Toast.makeText(context, authTask.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun insertHistoryToFirebase(foodHistory: DataHistory, uid: String, context: Context) {
        val ref = FirebaseDatabase.getInstance().getReferenceFromUrl(BuildConfig.FIREBASE_ADDRESS)
        val historyId = ref.push().key
        val isiNotes = DataHistory(
            historyId,
            foodHistory.date,
            foodHistory.labels,
            foodHistory.calorie,
            foodHistory.sugar
        )

        if (historyId != null) {
            ref.child("Users").child(uid).child("History").child(historyId).setValue(isiNotes).addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(context, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun getAllFirebaseHistory(uid: String): LiveData<ArrayList<DataHistory>> {
        val noteResult = MutableLiveData<ArrayList<DataHistory>>()
        val ref = FirebaseDatabase.getInstance().getReferenceFromUrl(BuildConfig.FIREBASE_ADDRESS)
        ref.child("Users").child(uid).child("History").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val noteList = ArrayList<DataHistory>()
                if (snapshot.exists()){
                    noteList.clear()
                    for(data in snapshot.children){
                        val note = data.getValue(DataHistory::class.java)
                        if (note != null) {
                            noteList.add(note)
                        }
                        Log.d("Isi Data", note.toString())
                    }
                    noteResult.postValue(noteList)
                    Log.d("Isi Data", noteList.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        Log.d("Isi NoteResult", noteResult.toString())
        return noteResult
    }

    override fun deleteFirebase(historyId: String, uid: String, context: Context){
        val ref = FirebaseDatabase.getInstance(BuildConfig.FIREBASE_ADDRESS).getReference("Users")
        ref.child(uid).child("History").child(historyId).removeValue().addOnCompleteListener{
            if (it.isSuccessful){
                Toast.makeText(context, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}