package com.example.usergithubrepos.data

import com.example.usergithubrepos.models.repos.UserReposResponse
import com.example.usergithubrepos.models.userprofile.ProfileResponse
import com.example.usergithubrepos.service.ApiService
import org.koin.core.KoinComponent
import org.koin.core.inject

class UserRepository : KoinComponent {

    private val apiService: ApiService by inject()

    /**
     * Request data from backend
     */

    suspend fun getUserProfileData(userName: String): ProfileResponse {
        return apiService.getUserDetail(userName)
    }

    suspend fun getUserReposData(userName: String): List<UserReposResponse> {
        return apiService.getUserRepos(userName)
    }
}