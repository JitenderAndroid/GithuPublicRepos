package com.example.usergithubrepos.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.usergithubrepos.base.BaseUTTest
import com.example.usergithubrepos.data.UserUseCase
import com.example.usergithubrepos.data.UserViewModel
import com.example.usergithubrepos.di.configureTestAppComponent
import com.example.usergithubrepos.models.userprofile.ProfileResponse
import com.example.usergithubrepos.service.LiveDataWrapper
import com.google.gson.Gson
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.startKoin

@RunWith(JUnit4::class)
class LoginActivityViewModelTest: BaseUTTest() {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var mUserViewModel: UserViewModel

    val mDispatcher = Dispatchers.Unconfined

    @MockK
    lateinit var mUserUseCase: UserUseCase

    val mParam = "jitenderAndroid"

    @Before
    fun start(){
        super.setUp()
        //Used for initiation of Mockk
        MockKAnnotations.init(this)
        //Start Koin with required dependencies
        startKoin{ modules(configureTestAppComponent(getMockWebServerUrl()))}
    }

    @Test
    fun test_login_view_model_data_populates_expected_value(){

        mUserViewModel = UserViewModel(mDispatcher, mDispatcher, mUserUseCase)
        val sampleResponse = getJson("success_resp_list.json")
        var jsonObj = Gson().fromJson(sampleResponse, ProfileResponse::class.java)
        //Make sure login use case returns expected response when called
        coEvery { mUserUseCase.getUserProfile(any()) } returns jsonObj
        mUserViewModel.mUserData.observeForever {  }

        mUserViewModel.fetchUserRepos(mParam)

        assert(mUserViewModel.mUserData.value != null)
        assert(
            mUserViewModel.mUserData.value!!.responseStatus
                == LiveDataWrapper.RESPONSESTATUS.SUCCESS)
        val testResult = mUserViewModel.mUserData.value as LiveDataWrapper<ProfileResponse>
        assertEquals(testResult.response!!.name, mParam)
    }
}