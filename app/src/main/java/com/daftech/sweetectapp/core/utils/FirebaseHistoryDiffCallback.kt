package com.daftech.sweetectapp.core.utils

import androidx.recyclerview.widget.DiffUtil
import com.daftech.sweetectapp.core.data.DataHistory
import com.daftech.sweetectapp.core.data.FoodData

class FirebaseHistoryDiffCallback(private val mOldHistory: List<DataHistory>, private val mNewHistoryList: List<DataHistory>): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldHistory.size
    }

    override fun getNewListSize(): Int {
       return mNewHistoryList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mNewHistoryList[oldItemPosition].id == mNewHistoryList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = mOldHistory[oldItemPosition]
        val newEmployee = mNewHistoryList[newItemPosition]
        return oldEmployee.labels == newEmployee.labels && oldEmployee.calorie == newEmployee.calorie && oldEmployee.sugar == newEmployee.sugar
    }

}