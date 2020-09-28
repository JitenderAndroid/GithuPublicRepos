package com.example.usergithubrepos.data

import com.example.usergithubrepos.models.repos.UserReposResponse
import com.example.usergithubrepos.models.userprofile.ProfileResponse
import org.koin.core.KoinComponent
import org.koin.core.inject

class UserUseCase : KoinComponent {

    private val mUserRepository: UserRepository by inject()

    suspend fun getUserProfile(userName: String) :  ProfileResponse {
        return mUserRepository.getUserProfileData(userName)
    }

    suspend fun getUserRepos(userName: String) : List<UserReposResponse> {
        return mUserRepository.getUserReposData(userName)
    }
}