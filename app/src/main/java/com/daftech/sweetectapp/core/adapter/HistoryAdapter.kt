package com.daftech.sweetectapp.core.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.daftech.sweetectapp.core.data.DataHistory
import com.daftech.sweetectapp.core.repository.FoodRepository
import com.daftech.sweetectapp.core.utils.FirebaseHistoryDiffCallback
import com.daftech.sweetectapp.databinding.ItemHistoryBinding

class HistoryAdapter(private val uid: String, private val context: Context): RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>(){

    private val listHistory = ArrayList<DataHistory>()
    private val foodRepository: FoodRepository? = null

    fun setListHistory(listHistory: List<DataHistory>){
        val diffCallback = FirebaseHistoryDiffCallback(this.listHistory, listHistory)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.listHistory.clear()
        this.listHistory.addAll(listHistory)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): HistoryAdapter.HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryAdapter.HistoryViewHolder, position: Int) {
        holder.bind(listHistory[position])
    }

    override fun getItemCount(): Int {
        return listHistory.size
    }

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(history: DataHistory){
            with(binding){
                tvItemDate.text = history.date
                tvItemTitle.text = history.labels
                tvItemCalorie.text = history.calorie
                tvItemSugar.text = history.sugar

                if (history.id != null){
                    btnDelete.setOnClickListener {
                        foodRepository?.deleteFirebase(history.id!!, uid, context)
                        Log.d("isi Id", history.id!!)
                    }
                }
            }
        }
    }
}