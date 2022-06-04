package com.example.mylotto.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.mylotto.R
import com.example.mylotto.databinding.ItemLoadingBinding
import com.example.mylotto.databinding.RecordListItemBinding
import com.example.mylotto.main.OnItemClick
import com.example.mylotto.main.viewmodel.PurchaseListViewModel
import com.example.mylotto.util.room.Lotto

class PurchaseListAdapter(
    viewModel: PurchaseListViewModel,
    private var list: MutableList<Lotto?>,
    itemClick: OnItemClick
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }

    var mCallback: OnItemClick? = null
    var viewModel2: PurchaseListViewModel? = null

    init {
        mCallback = itemClick
        viewModel2 = viewModel
    }

    fun setList(list2: MutableList<Lotto?>?) {
        if (list2 != null) {
            list = list2
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position]?.number) {
            null -> VIEW_TYPE_LOADING
            else -> VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                val binding = RecordListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                RecordViewHolder(binding)
            }
            else -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemLoadingBinding.inflate(layoutInflater, parent, false)
                LoadingViewHolder(binding)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RecordViewHolder) {
            if (list[position] == null) {
                return
            }
            holder.bind(list[position])
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class RecordViewHolder(private val binding: RecordListItemBinding?) :
        RecyclerView.ViewHolder(binding?.root!!) {

        fun bind(item: Lotto?) {
            if (binding == null) {
                return
            }
            if (item == null) {
                return
            }

            binding.number.text = item.number
            binding.time.text = item.time
            binding.seq.text = item.seq

            if (!item.checked) {
                binding.check.setImageResource(R.drawable.lotto_false)
            } else {
                binding.check.setImageResource(R.drawable.lotto_true)
            }

            binding.modify.setOnClickListener {
                mCallback?.onModifyClick(item, adapterPosition)
            }

            binding.delete.setOnClickListener {
//                viewModel2?.test2()
                mCallback?.onDeleteClick(item.number, item.time, adapterPosition)
            }

        }

    }

    inner class LoadingViewHolder(private val binding: ItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

}