package com.example.contactauth.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.contactauth.data.local.room.entity.ContactEntity
import com.example.contactauth.databinding.ItemContactBinding

class ContactAdapter : ListAdapter<ContactEntity, ContactAdapter.ViewHolder>(CallBack){

    private var editListener: ((ContactEntity) -> Unit)? = null
    private var deleteListener: ((ContactEntity) -> Unit)? = null

    fun submitEdit(block: (ContactEntity) -> Unit) {
        editListener = block
    }

    fun submitDelete(block: (ContactEntity) -> Unit) {
        deleteListener = block
    }

    inner class ViewHolder(val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            binding.delete.setOnClickListener {
                deleteListener?.invoke(getItem(absoluteAdapterPosition))
            }
            binding.edit.setOnClickListener {
                editListener?.invoke(getItem(absoluteAdapterPosition))
            }
        }

        fun bind() {
            binding.name.text = getItem(absoluteAdapterPosition).name
            binding.number.text = getItem(absoluteAdapterPosition).phone
        }
    }

    object CallBack : DiffUtil.ItemCallback<ContactEntity>() {
        override fun areItemsTheSame(oldItem: ContactEntity, newItem: ContactEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ContactEntity, newItem: ContactEntity): Boolean {
            return oldItem.name == newItem.name && oldItem.id == newItem.id && oldItem.phone == newItem.phone
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()
}