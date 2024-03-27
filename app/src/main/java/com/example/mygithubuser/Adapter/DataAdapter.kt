package com.example.mygithubuser.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mygithubuser.Activity.DetailUserActivity
import com.example.mygithubuser.Data.remote.response.DetailResponse
import com.example.mygithubuser.databinding.CardUserBinding

class DataAdapter(private val listData: List<DetailResponse>) :
    RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = listData[position]
        holder.binding.tvName.text = user.login


        Glide.with(holder.itemView.context)
            .load(user.avatar_url)
            .circleCrop()
            .into(holder.binding.profileImage)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val moveIntent = Intent(context, DetailUserActivity::class.java)
            moveIntent.putExtra(DetailUserActivity.EXTRA_USERNAME, user.login)
            context.startActivity(moveIntent)
        }
    }

    override fun getItemCount() = listData.size

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DetailResponse)
    }

    class ViewHolder(val binding: CardUserBinding) : RecyclerView.ViewHolder(binding.root)
}