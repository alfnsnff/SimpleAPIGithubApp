package com.example.githubapis.ui.favoriteUser

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubapis.data.response.GithubResponse
import com.example.githubapis.database.FavoriteUser
import com.example.githubapis.repository.FavoriteUserRepository

class FavoriteUserViewModel(application: Application) : ViewModel() {
    private val mFavoriteUserRepository: FavoriteUserRepository =
        FavoriteUserRepository(application)

    private val _users = MutableLiveData<GithubResponse>()
    val users: LiveData<GithubResponse> = _users

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>> =
        mFavoriteUserRepository.getAllFavoriteUsers()

}