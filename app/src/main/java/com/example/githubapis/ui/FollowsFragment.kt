package com.example.githubapis.ui

import ReviewAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapis.data.response.User
import com.example.githubapis.databinding.FragmentFollowsBinding

class FollowsFragment : Fragment() {

    private lateinit var binding: FragmentFollowsBinding
    private lateinit var viewModel: FollowsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[FollowsViewModel::class.java]
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvFollows.layoutManager = layoutManager
        binding.rvFollows.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                layoutManager.orientation
            )
        )


        arguments?.let {
            val position = it.getInt(ARG_POSITION)
            val username = it.getString(ARG_USERNAME)
            if (position == 1) {
                viewModel.getUserFollowers(username.toString())
            } else {
                viewModel.getUserFollowing(username.toString())
            }
        }

        viewModel.userFollowers.observe(viewLifecycleOwner) { lists ->
            setFollowersData(lists)
        }

        viewModel.userFollowing.observe(viewLifecycleOwner) { lists ->
            setFollowingData(lists)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            showErrorMessage(errorMessage)
        }
    }

    private fun setFollowersData(response: List<User>) {
        val adapter = ReviewAdapter()
        adapter.submitList(response)
        binding.rvFollows.adapter = adapter
    }

    private fun setFollowingData(response: List<User>) {
        val adapter = ReviewAdapter()
        adapter.submitList(response)
        binding.rvFollows.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showErrorMessage(errMessage: String) {
        binding.errMessage.text = errMessage
    }

    companion object {
        const val ARG_POSITION = "position_number"
        const val ARG_USERNAME = "username"
    }
}
