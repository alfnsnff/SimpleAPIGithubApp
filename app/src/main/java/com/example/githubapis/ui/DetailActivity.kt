package com.example.githubapis.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubapis.R
import com.example.githubapis.data.response.DetailUserResponse
import com.example.githubapis.database.FavoriteUser
import com.example.githubapis.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val username = intent.getStringExtra(USERNAME).toString()
        val avatarUrl = intent.getStringExtra(AVATAR_URL).toString()

        viewModel.getUserDetail(username)

        viewModel.usersDetail.observe(this) { users ->
            setUserDetailData(users)
        }
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
        viewModel.errorMessage.observe(this) {
            showErrorMessage(it)
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username

        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        viewModel.getFavoriteUserByUsername(username).observe(this) { favoriteUser ->
            if (favoriteUser != null) {
                binding.favAdd.setImageResource(R.drawable.baseline_favorite_24)
            }
            binding.favAdd.setOnClickListener {
                if (favoriteUser == null) {
                    viewModel.insert(FavoriteUser(username = username, avatarUrl = avatarUrl))
                } else {
                    viewModel.delete(FavoriteUser(username = username, avatarUrl = avatarUrl))
                    binding.favAdd.setImageResource(R.drawable.baseline_favorite_border_24)
                }
            }
        }
    }

    private fun setUserDetailData(userDetail: DetailUserResponse) {
        Glide.with(binding.profileImage.context)
            .load(userDetail.avatarUrl)
            .into(binding.profileImage)
        binding.tvDetailName.text = userDetail.name
        binding.tvDetailRole.text = userDetail.login
        val text = "${userDetail.followers} Followers ${userDetail.following} Following"
        binding.tvFollows.text = text
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
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

    private fun showErrorMessage(errMessage: String) {
        binding.errMessage.text = errMessage
    }

    companion object {
        const val USERNAME = "user_name"
        const val AVATAR_URL = "www.example.com"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

}