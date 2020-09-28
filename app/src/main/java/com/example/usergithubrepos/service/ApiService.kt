package com.example.usergithubrepos.service

import com.example.usergithubrepos.models.repos.UserReposResponse
import com.example.usergithubrepos.models.userprofile.ProfileResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("users/{username}")
    suspend fun getUserDetail(@Path("username") userName: String): ProfileResponse

    @GET("users/{username}/repos")
    suspend fun getUserRepos(@Path("username") userName: String): List<UserReposResponse>
}