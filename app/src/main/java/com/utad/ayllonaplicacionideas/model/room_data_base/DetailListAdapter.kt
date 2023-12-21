package com.utad.ayllonaplicacionideas.model.room_data_base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.utad.ayllonaplicacionideas.databinding.ItemDetailListBinding
import com.utad.ayllonaplicacionideas.model.room_data_base.entities.Detail


class DetailListAdapter(
    val deleteDetail: (detail: Detail, position: Int) -> Unit)
    : ListAdapter<Detail, DetailListAdapter.DetailListViewHolder>(DetailListItemCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDetailListBinding.inflate(inflater, parent, false)
        return DetailListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailListViewHolder, position: Int) {
        val detail = getItem(position)
        holder.binding.tvDetailInfoItem.text = detail.descripcion

        holder.binding.tvDetailInfoItem.setOnLongClickListener {
            deleteDetail(detail, position)
            true
        }

    }


    object DetailListItemCallBack: DiffUtil.ItemCallback<Detail>() {
        override fun areItemsTheSame(oldItem: Detail, newItem: Detail): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Detail, newItem: Detail): Boolean {
            return oldItem == newItem
        }

    }

    inner class DetailListViewHolder(val binding: ItemDetailListBinding): RecyclerView.ViewHolder(binding.root)

}