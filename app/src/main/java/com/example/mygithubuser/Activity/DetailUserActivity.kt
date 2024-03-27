package com.example.mygithubuser.Activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.mygithubuser.Adapter.SectionsPagerAdapter
import com.example.mygithubuser.Data.Resource
import com.example.mygithubuser.Data.remote.response.DetailResponse
import com.example.mygithubuser.R
import com.example.mygithubuser.ViewModel.DetailViewModel
import com.example.mygithubuser.ViewModelFactory.ViewModelFactory
import com.example.mygithubuser.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailUserActivity : AppCompatActivity() {
    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
        const val EXTRA_USERNAME = "extra_username"
    }

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var login: String
    private var favStatus: Boolean = false
    val factory = ViewModelFactory.getInstance(this@DetailUserActivity)
    val detailViewModel: DetailViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.detail_user)

        login = intent?.getStringExtra(EXTRA_USERNAME) ?: ""

        detailViewModel.apply {
            getDetail(login).observe(this@DetailUserActivity) {
                when (it) {
                    is Resource.Success -> {
                        setListData(it.data)
                        showLoading(false)
                    }
                    is Resource.Error -> {
                        showLoading(false)
                    }
                    is Resource.Loading -> showLoading(true)
                }
            }
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this, login)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

        binding.fab.setOnClickListener {
            detailViewModel.apply {
                lifecycleScope.launch(Dispatchers.IO) {
                    favStatus = isFavorite(login)
                }
                getDetail(login).observe(this@DetailUserActivity) {
                    when (it) {
                        is Resource.Success -> {
                            val user: DetailResponse = it.data
                            when (favStatus) {
                                true -> {
                                    deleteFavUser(user)
                                    inactiveFavoriteButton()
                                    Toast.makeText(
                                        this@DetailUserActivity,
                                        getString(R.string.BerhasilHapus),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                false -> {
                                    setFavUser(user)
                                    activeFavoriteButton()
                                    Toast.makeText(
                                        this@DetailUserActivity,
                                        getString(R.string.BerhasilTambah),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            showLoading(false)
                        }
                        is Resource.Error -> {
                            showLoading(false)
                        }
                        is Resource.Loading -> showLoading(true)
                    }
                }
            }
        }
        setIconFav()
    }


    private fun setIconFav() {
        detailViewModel.apply {
            lifecycleScope.launch(Dispatchers.IO) {
                favStatus = isFavorite(login)
            }
            getDetail(login).observe(this@DetailUserActivity) {
                when (it) {
                    is Resource.Success -> {
                        when (favStatus) {
                            true -> activeFavoriteButton()
                            false -> inactiveFavoriteButton()
                        }
                        showLoading(false)
                    }
                    is Resource.Error -> {
                        showLoading(false)
                    }
                    is Resource.Loading -> showLoading(true)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        pressButton(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun pressButton(index: Int) {
        when (index) {
            R.id.settings -> {
                val settingsIntent = Intent(this@DetailUserActivity, SettingsActivity::class.java)
                startActivity(settingsIntent)
            }
            R.id.fav -> {
                val favIntent = Intent(this@DetailUserActivity, FavActivity::class.java)
                startActivity(favIntent)
            }
        }
    }

    private fun setListData(data: DetailResponse) {
        binding.tvUsername.text = data.login
        binding.tvName.text = data.name
        binding.tvFollowers.text = data.followers.toString() + " Followers"
        binding.tvFollowing.text = data.following.toString() + " Following"
        Glide.with(this@DetailUserActivity)
            .load(data.avatar_url)
            .circleCrop()
            .into(binding.profileImage)
    }


    private fun activeFavoriteButton() {
        binding.fab.setImageResource(R.drawable.baseline_bookmark_24)
    }

    private fun inactiveFavoriteButton() {
        binding.fab.setImageResource(R.drawable.baseline_bookmark_border_24)
    }


    fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}