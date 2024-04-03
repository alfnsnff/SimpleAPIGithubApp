package com.example.githubapis.data.retrofit

import com.example.githubapis.data.response.GithubResponse
import com.example.githubapis.data.response.DetailUserResponse
import com.example.githubapis.data.response.User
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getUsername(
        @Query("q") q: String
    ): Call<GithubResponse>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<User>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<User>>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<DetailUserResponse>
}