package com.example.githubapis.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubapis.data.response.User
import com.example.githubapis.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FollowsViewModel : ViewModel() {

    private val _userFollowers = MutableLiveData<List<User>>()
    val userFollowers: LiveData<List<User>> = _userFollowers

    private val _userFollowing = MutableLiveData<List<User>>()
    val userFollowing: LiveData<List<User>> = _userFollowing

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getUserFollowers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<User>> {
            override fun onResponse(
                call: Call<List<User>>,
                response: Response<List<User>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userFollowers.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Error: Something went wrong. Please Try Again Later"
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getUserFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<User>> {
            override fun onResponse(
                call: Call<List<User>>,
                response: Response<List<User>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userFollowing.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Error: Something went wrong. Please Try Again Later"
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "FollowsViewModel"
    }

}