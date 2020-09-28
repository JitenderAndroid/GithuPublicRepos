package com.example.usergithubrepos.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.usergithubrepos.base.BaseUTTest
import com.example.usergithubrepos.data.UserRepository
import com.example.usergithubrepos.data.UserUseCase
import com.example.usergithubrepos.di.configureTestAppComponent
import com.example.usergithubrepos.service.ApiService
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.startKoin
import org.koin.core.inject
import java.net.HttpURLConnection

@RunWith(JUnit4::class)
class LoginUseCaseTest : BaseUTTest() {

    //Target
    private lateinit var mUserUseCase: UserUseCase
    //Inject login repo created with koin
    val mLoginRepo : UserRepository by inject()
    //Inject api service created with koin
    val mAPIService : ApiService by inject()
    //Inject Mockwebserver created with koin
    val mockWebServer : MockWebServer by inject()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    val mNextValue = "https://swapi.co/api/people/?page=2"
    val mParam = "1"
    val mCount = 87

    @Before
    fun start(){
        super.setUp()
        //Start Koin with required dependencies
        startKoin{ modules(configureTestAppComponent(getMockWebServerUrl()))}
    }

    @Test
    fun test_login_use_case_returns_expected_value()= runBlocking{

        mockNetworkResponseWithFileContent("success_resp_list.json", HttpURLConnection.HTTP_OK)
        mUserUseCase = UserUseCase()

        val dataReceived = mUserUseCase.getUserProfile(mParam)

        assertNotNull(dataReceived)
        assertEquals(dataReceived.name, mCount)
        assertEquals(dataReceived.company, mNextValue)
    }
}