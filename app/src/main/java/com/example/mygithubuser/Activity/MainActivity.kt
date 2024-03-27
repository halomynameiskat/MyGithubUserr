package com.example.mygithubuser.Activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygithubuser.Adapter.DataAdapter
import com.example.mygithubuser.Data.Resource
import com.example.mygithubuser.Data.remote.response.DetailResponse
import com.example.mygithubuser.DataStore.SettingPreferences
import com.example.mygithubuser.R
import com.example.mygithubuser.ViewModel.MainViewModel
import com.example.mygithubuser.ViewModel.ThemeViewModel
import com.example.mygithubuser.ViewModelFactory.PrefModelFactory
import com.example.mygithubuser.ViewModelFactory.ViewModelFactory
import com.example.mygithubuser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val pref = SettingPreferences.getInstance(dataStore)
        val themeViewModel = ViewModelProvider(this, PrefModelFactory(pref)).get(
            ThemeViewModel::class.java
        )

        themeViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.rvUsers.setHasFixedSize(true)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this@MainActivity)
        val mainViewModel: MainViewModel by viewModels {
            factory
        }

        binding.search.apply {
            queryHint = context.getString(R.string.Searchdisini)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    mainViewModel.searchUser(query).observe(this@MainActivity) {
                        when (it) {
                            is Resource.Loading -> showLoading(true)
                            is Resource.Error -> {
                                showLoading(false)
                            }
                            is Resource.Success -> {
                                showLoading(false)
                                setListData(it.data)
                            }
                        }
                    }
                    clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText == null || newText.isEmpty()) {
                        binding.rvUsers.visibility = View.INVISIBLE
                    }
                    return true
                }
            })
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
                val settingsIntent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(settingsIntent)
            }
            R.id.fav -> {
                val favIntent = Intent(this@MainActivity, FavActivity::class.java)
                startActivity(favIntent)
            }
        }
    }

    private fun showLoading(isLoading: Boolean?) {
        binding.apply {
            progressBar.visibility = if (isLoading == true) View.VISIBLE else View.INVISIBLE
            rvUsers.visibility = if (isLoading == true) View.INVISIBLE else View.VISIBLE
        }
    }

    private fun setListData(list: List<DetailResponse>) {
        val listDataAdapter = DataAdapter(list)
        binding.rvUsers.adapter = listDataAdapter
    }
}