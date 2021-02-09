package com.etsproject.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.etsproject.data.Profile
import com.etsproject.databinding.ProfileItemLayoutBinding
import com.etsproject.utils.ClickCallback

class ProfileAdapter(private val listener: ClickCallback) :
    ListAdapter<Profile, ProfileAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ProfileItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ProfileItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val profile = getItem(position)
                    listener.onItemClick(profile)
                }
            }
        }

        fun bind(profile: Profile) {
            binding.profile = profile
            binding.executePendingBindings()
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Profile>() {
        override fun areItemsTheSame(oldItem: Profile, newItem: Profile) =
            oldItem.pk == newItem.pk

        override fun areContentsTheSame(oldItem: Profile, newItem: Profile) =
            oldItem == newItem
    }
}