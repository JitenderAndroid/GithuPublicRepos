package com.example.usergithubrepos.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.usergithubrepos.data.UserUseCase
import com.example.usergithubrepos.data.UserViewModel
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Base VM Factory for creation of different VM's
 */
class BaseViewModelFactory constructor(
    private val mainDispatcher: CoroutineDispatcher
    ,private val ioDispatcher: CoroutineDispatcher
    ,private val userUseCase: UserUseCase) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            UserViewModel(mainDispatcher, ioDispatcher, userUseCase) as T
        } else {
            throw IllegalArgumentException("ViewModel Not configured") as Throwable
        }
    }
}