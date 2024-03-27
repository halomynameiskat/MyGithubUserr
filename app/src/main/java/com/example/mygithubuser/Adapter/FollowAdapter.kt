package com.example.mygithubuser.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mygithubuser.Activity.DetailUserActivity
import com.example.mygithubuser.Data.remote.response.FollowResponse
import com.example.mygithubuser.databinding.CardUserBinding

class FollowAdapter(val listData: List<FollowResponse>) :
    RecyclerView.Adapter<FollowAdapter.ListViewHolder>() {

    class ListViewHolder(var binding: CardUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = CardUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listData[position]
        holder.binding.tvName.text = data.login
        Glide.with(holder.itemView.context)
            .load(data.avatarUrl)
            .into(holder.binding.profileImage)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val moveIntent = Intent(context, DetailUserActivity::class.java)
            moveIntent.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
            context.startActivity(moveIntent)
        }
    }
}