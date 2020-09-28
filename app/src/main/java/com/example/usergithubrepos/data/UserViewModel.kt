package com.example.usergithubrepos.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.usergithubrepos.models.repos.UserReposResponse
import com.example.usergithubrepos.models.userprofile.ProfileResponse
import com.example.usergithubrepos.service.LiveDataWrapper
import kotlinx.coroutines.*
import org.koin.core.KoinComponent

class UserViewModel(
    mainDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    private val userUseCase: UserUseCase
) : ViewModel(), KoinComponent {

    var mUserData = MutableLiveData<LiveDataWrapper<ProfileResponse>>()
    var mUserReposList = MutableLiveData<LiveDataWrapper<List<UserReposResponse>>>()

    private val job = SupervisorJob()
    private val mUiScope = CoroutineScope(mainDispatcher + job)
    private val mIoScope = CoroutineScope(ioDispatcher + job)

    //can be called from View explicitly if required
    //Keep default scope
    fun fetchUserProfile(userName: String) {
        //cancelJob()
        mUiScope.launch {
            mUserData.value = LiveDataWrapper.loading()

            try {
                val data = mIoScope.async {
                    return@async userUseCase.getUserProfile(userName)
                }.await()
                try {
                    mUserData.value = LiveDataWrapper.success(data)
                } catch (e: Exception) {}
            } catch (e: Exception) {
                mUserData.value = LiveDataWrapper.error(e)
            }
        }
    }

    fun fetchUserRepos(userName: String) {
        mUiScope.launch {
            mUserReposList.value = LiveDataWrapper.loading()

            try {
                val data = mIoScope.async {
                    return@async userUseCase.getUserRepos(userName)
                }.await()
                try {
                    mUserReposList.value = LiveDataWrapper.success(data)
                } catch (e: Exception) {
                    Log.e("exception", e.toString())
                }
            } catch (e: Exception) {
                mUserReposList.value = LiveDataWrapper.error(e)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        cancelJob()
    }

    private fun cancelJob() {
        this.job.cancel()
    }
}