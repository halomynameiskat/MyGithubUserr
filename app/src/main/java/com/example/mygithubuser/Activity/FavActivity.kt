package com.example.mygithubuser.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygithubuser.Adapter.DataAdapter
import com.example.mygithubuser.Data.remote.response.DetailResponse
import com.example.mygithubuser.R
import com.example.mygithubuser.ViewModel.FavoriteViewModel
import com.example.mygithubuser.ViewModelFactory.ViewModelFactory
import com.example.mygithubuser.databinding.ActivityFavBinding

class FavActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.FavoriteUsers)


        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val favoriteViewModel: FavoriteViewModel by viewModels {
            factory
        }

        val layoutManager =
            LinearLayoutManager(this@FavActivity, LinearLayoutManager.VERTICAL, false)
        binding.rvFav.layoutManager = layoutManager

        favoriteViewModel.getAllChanges().observe(this) {
            setListData(it)
        }
    }

    private fun setListData(listGithub: List<DetailResponse>) {
        val adapter = DataAdapter(listGithub)
        adapter.setOnItemClickCallback(object : DataAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DetailResponse) {
                val moveIntent = Intent(this@FavActivity, DetailUserActivity::class.java)
                moveIntent.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                startActivity(moveIntent)
            }
        })
        binding.rvFav.adapter = adapter
    }
}