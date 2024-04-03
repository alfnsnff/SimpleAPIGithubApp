package com.example.githubapis.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapis.databinding.ActivityFavoriteUserBinding

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUserBinding
    private lateinit var adapter: FavoriteUserAdapter // Use ReviewAdapter instead of FavoriteUserAdapter

    private val viewModel by viewModels<FavoriteUserViewModel>{
        ViewModelFactory.getInstance(application)
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
