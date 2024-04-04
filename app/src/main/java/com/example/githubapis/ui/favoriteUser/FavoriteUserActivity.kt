package com.example.githubapis.ui.favoriteUser

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapis.databinding.ActivityFavoriteUserBinding
import com.example.githubapis.helper.ViewModelFactory
import com.example.githubapis.ui.settings.SettingPreferences
import com.example.githubapis.ui.settings.dataStore

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUserBinding
    private lateinit var adapter: FavoriteUserAdapter // Use com.example.githubapis.ui.main.ReviewAdapter instead of FavoriteUserAdapter

    private val viewModel by viewModels<FavoriteUserViewModel> {
        val pref = SettingPreferences.getInstance(application.dataStore)
        ViewModelFactory.getInstance(application, pref)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val layoutManager = LinearLayoutManager(this)
        binding.rvFavUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvFavUsers.addItemDecoration(itemDecoration)

        adapter = FavoriteUserAdapter()
        binding.rvFavUsers.adapter = adapter

        viewModel.getAllFavoriteUsers().observe(this) { favUser ->
            if (favUser != null) {
                adapter.setListUsers(favUser)
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
