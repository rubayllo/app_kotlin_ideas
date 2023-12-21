package com.utad.ayllonaplicacionideas.model.room_data_base

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.utad.ayllonaplicacionideas.databinding.ItemIdeasListBinding
import com.utad.ayllonaplicacionideas.model.room_data_base.entities.Idea

class IdeasListAdapter(
    val goToInfoIdea: (ideaId: Int) -> Unit,
    val removeIdea: (idea: Idea, position: Int) -> Unit
) : ListAdapter<Idea, IdeasListAdapter.IdeasListViewHolder>(
    IdeasListItemCallBack
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IdeasListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemIdeasListBinding.inflate(inflater, parent, false)
        return IdeasListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IdeasListViewHolder, position: Int) {
        val idea = getItem(position)
        Log.d("Estado", "onBindViewHolder: por aqui pasa")
        holder.binding.tvTittleIdea.text = idea.nombre
        holder.binding.tvDescripIdea.text = idea.descripcion
        holder.binding.tvStatus.text = idea.estado
        holder.binding.tvPriority.text = idea.prioridad

        when {
            idea.prioridad.equals("Baja") ->
                holder.binding.tvPriority.backgroundTintList =
                    ColorStateList.valueOf(Color.GREEN)

            idea.prioridad.equals("Media") ->
                holder.binding.tvPriority.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#FFB865"))

            idea.prioridad.equals("Alta") ->
                holder.binding.tvPriority.backgroundTintList =
                    ColorStateList.valueOf(Color.RED)
        }

        holder.binding.root.setOnClickListener {
            goToInfoIdea(idea.id)
        }
        holder.binding.btnDeleteIdea.setOnClickListener {
            removeIdea(idea, position)
        }
    }


    inner class IdeasListViewHolder(val binding: ItemIdeasListBinding) :
        RecyclerView.ViewHolder(binding.root)

    object IdeasListItemCallBack : DiffUtil.ItemCallback<Idea>() {

        override fun areItemsTheSame(oldItem: Idea, newItem: Idea): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Idea, newItem: Idea): Boolean {
            return oldItem == newItem
        }
    }

}