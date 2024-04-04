package com.example.githubapis.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapis.R
import com.example.githubapis.data.response.GithubResponse
import com.example.githubapis.databinding.ActivityMainBinding
import com.example.githubapis.helper.ViewModelFactory
import com.example.githubapis.ui.detail.DetailActivity
import com.example.githubapis.ui.favoriteUser.FavoriteUserActivity
import com.example.githubapis.ui.settings.SettingPreferences
import com.example.githubapis.ui.settings.SettingsActivity
import com.example.githubapis.ui.settings.SettingsViewModel
import com.example.githubapis.ui.settings.dataStore


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> {
        val pref = SettingPreferences.getInstance(application.dataStore)
        ViewModelFactory.getInstance(application, pref)
    }

    private val settingsViewModel by viewModels<SettingsViewModel> {
        val pref = SettingPreferences.getInstance(application.dataStore)
        ViewModelFactory.getInstance(application, pref)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingsViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            applyTheme(isDarkModeActive)
        }

        viewModel.users.observe(this) { users ->
            setUsersData(users)
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.setText(searchView.text)
                    viewModel.findUsers(searchView.text.toString().trim())
                    searchView.hide()
                    false
                }
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvReview.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvReview.addItemDecoration(itemDecoration)

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.errorMessage.observe(this) {
            showErrorMessage(it)
        }

    }

    private fun applyTheme(isDarkModeActive: Boolean) {
        if (isDarkModeActive) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
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
                val moveIntent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(moveIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}