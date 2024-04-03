package com.example.githubapis.ui

import ReviewAdapter
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapis.R
import com.example.githubapis.data.response.GithubResponse
import com.example.githubapis.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[MainViewModel::class.java]

        mainViewModel.users.observe(this) { users ->
            setUsersData(users)
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.setText(searchView.text)
                    mainViewModel.findUsers(searchView.text.toString().trim())
                    searchView.hide()
                    false
                }
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvReview.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvReview.addItemDecoration(itemDecoration)

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainViewModel.errorMessage.observe(this) {
            showErrorMessage(it)
        }

    }

    private fun setUsersData(response: GithubResponse) {
        val adapter = ReviewAdapter()
        adapter.submitList(response.users)
        binding.rvReview.adapter = adapter
        adapter.setOnItemClickListener { user ->
            val moveWithDataIntent = Intent(this@MainActivity, DetailActivity::class.java)
            moveWithDataIntent.putExtra(DetailActivity.USERNAME, user.login)
            moveWithDataIntent.putExtra(DetailActivity.AVATAR_URL, user.avatarUrl)
            startActivity(moveWithDataIntent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showErrorMessage(errMessage: String) {
        binding.errMessage.text = errMessage
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.fav_list -> {
                val moveIntent = Intent(this@MainActivity, FavoriteUserActivity::class.java)
                startActivity(moveIntent)
            }
            R.id.settings -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }
}