package com.daftech.sweetectapp.core.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.daftech.sweetectapp.core.data.DataHistory
import com.daftech.sweetectapp.core.utils.FirebaseHistoryDiffCallback
import com.daftech.sweetectapp.databinding.ItemHistoryBinding

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>(){

    private val listHistory = ArrayList<DataHistory>()

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

    fun getSwippedData(position: Int): DataHistory? = listHistory[position]

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
            }
        }
    }
}