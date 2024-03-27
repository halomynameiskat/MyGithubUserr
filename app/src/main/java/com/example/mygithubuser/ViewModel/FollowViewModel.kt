package com.example.mygithubuser.ViewModel

import androidx.lifecycle.ViewModel
import com.example.mygithubuser.Data.Repository


class FollowViewModel(private val repository: Repository) : ViewModel() {

    fun getFollower(name: String) = repository.followerUser(name)

    fun getFollowing(name: String) = repository.followingUser(name)

}