package com.example.mygithubuser.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygithubuser.Adapter.FollowAdapter
import com.example.mygithubuser.Data.Resource
import com.example.mygithubuser.Data.remote.response.FollowResponse
import com.example.mygithubuser.ViewModel.FollowViewModel
import com.example.mygithubuser.ViewModelFactory.ViewModelFactory
import com.example.mygithubuser.databinding.FragmentFollowBinding


class FollowFragment : Fragment() {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvFollow.layoutManager = layoutManager

        val argsName = this.arguments?.getString(EXTRA_NAME)
        val argsNumber = this.arguments?.getInt(ARG_SECTION_NUMBER)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val followViewModel: FollowViewModel by viewModels {
            factory
        }

        if (argsName != null) {
            when (argsNumber) {
                0 -> {
                    followViewModel.getFollower(argsName).observe(viewLifecycleOwner) {
                        when (it) {
                            is Resource.Loading -> showLoading(true)
                            is Resource.Error -> {
                                showLoading(false)
                            }
                            is Resource.Success -> {
                                showLoading(false)
                                setFollower(it.data, argsName)
                            }
                        }
                    }
                }

                1 -> {
                    followViewModel.getFollowing(argsName).observe(viewLifecycleOwner) {
                        when (it) {
                            is Resource.Loading -> showLoading(true)
                            is Resource.Error -> {
                                showLoading(false)
                            }
                            is Resource.Success -> {
                                showLoading(false)
                                setFollowing(it.data, argsName)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setFollower(follower: List<FollowResponse>?, name: String) {
        val adapter = follower?.let { FollowAdapter(it) }
        binding.rvFollow.adapter = adapter
    }

    private fun setFollowing(following: List<FollowResponse>?, name: String) {
        val adapter = following?.let { FollowAdapter(it) }
        binding.rvFollow.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean?) {
        binding.apply {
            progressBar.visibility = if (isLoading == true) View.VISIBLE else View.INVISIBLE
            rvFollow.visibility = if (isLoading == true) View.INVISIBLE else View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        private const val EXTRA_NAME = "username"

        @JvmStatic
        fun newInstance(index: Int, username: String) = FollowFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_SECTION_NUMBER, index)
                putString(EXTRA_NAME, username)
            }
        }
    }
}