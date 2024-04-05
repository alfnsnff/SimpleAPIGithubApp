package com.example.githubapis.ui.favoriteUser

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapis.data.response.User
import com.example.githubapis.databinding.ActivityFavoriteUserBinding
import com.example.githubapis.helper.ViewModelFactory
import com.example.githubapis.ui.detail.DetailActivity
import com.example.githubapis.ui.main.UserAdapter
import com.example.githubapis.ui.settings.SettingPreferences
import com.example.githubapis.ui.settings.dataStore

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUserBinding
    private lateinit var adapter: UserAdapter // Use com.example.githubapis.ui.main.ReviewAdapter instead of FavoriteUserAdapter

    private val viewModel by viewModels<FavoriteUserViewModel> {
        val pref = SettingPreferences.getInstance(application.dataStore)
        ViewModelFactory.getInstance(application, pref)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val layoutManager = LinearLayoutManager(this)
        binding.rvFavUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvFavUsers.addItemDecoration(itemDecoration)

        adapter = UserAdapter()
        binding.rvFavUsers.adapter = adapter
        viewModel.getAllFavoriteUsers().observe(this) { users ->
            val items = arrayListOf<User>()
            users.map {
                val item = User(login = it.username, avatarUrl = it.avatarUrl)
                items.add(item)
            }
            adapter.submitList(items)
            adapter.setOnItemClickListener { user ->
                val moveWithDataIntent = Intent(this@FavoriteUserActivity, DetailActivity::class.java)
                moveWithDataIntent.putExtra(DetailActivity.USERNAME, user.login)
                moveWithDataIntent.putExtra(DetailActivity.AVATAR_URL, user.avatarUrl)
                startActivity(moveWithDataIntent)
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
