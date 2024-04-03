package com.example.githubapis.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubapis.database.FavoriteUser
import com.example.githubapis.databinding.ItemReviewBinding
import com.example.githubapis.helper.FavoriteUserDiffCallback

class FavoriteUserAdapter : RecyclerView.Adapter<FavoriteUserAdapter.FavUserViewHolder>() {
    private val listNotes = ArrayList<FavoriteUser>()
    fun setListUsers(listNotes: List<FavoriteUser>) {
        val diffCallback = FavoriteUserDiffCallback(this.listNotes, listNotes)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listNotes.clear()
        this.listNotes.addAll(listNotes)
        diffResult.dispatchUpdatesTo(this)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavUserViewHolder{
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavUserViewHolder(binding)
    }
    override fun onBindViewHolder(holder: FavUserViewHolder, position: Int) {
        holder.bind(listNotes[position])
    }
    override fun getItemCount(): Int {
        return listNotes.size
    }
    inner class FavUserViewHolder(private val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favoriteUser: FavoriteUser) {
            with(binding) {
                tvItem.text = favoriteUser.username
                Glide.with(binding.profileImage.context)
                    .load(favoriteUser.avatarUrl)
                    .into(binding.profileImage)
            }
        }
    }
}