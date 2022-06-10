package com.daftech.sweetectapp.core.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.daftech.sweetectapp.core.data.FoodData
import com.daftech.sweetectapp.core.utils.FirebaseHistoryDiffCallback
import com.daftech.sweetectapp.databinding.ItemHistoryBinding

class HistoryAdapter: RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>(){

    private var listHistory = ArrayList<FoodData>()

    fun setListHistory(listHistory: List<FoodData>){
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
        fun bind(history: FoodData){
            with(binding){
                tvItemTitle.text = history.labels
                tvItemCalorie.text = history.calorie
                tvItemSugar.text = history.sugar
            }
        }
    }
}