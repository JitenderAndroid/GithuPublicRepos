package com.example.usergithubrepos.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.usergithubrepos.base.BaseUTTest
import com.example.usergithubrepos.data.UserRepository
import com.example.usergithubrepos.di.configureTestAppComponent
import com.example.usergithubrepos.service.ApiService
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.startKoin
import org.koin.test.inject
import java.net.HttpURLConnection

@RunWith(JUnit4::class)
class LoginRepositoryTest : BaseUTTest(){

    //Target
    private lateinit var mRepo: UserRepository
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
    fun start() {
        super.setUp()

        startKoin{ modules(configureTestAppComponent(getMockWebServerUrl()))}
    }

    @Test
    fun test_login_repo_retrieves_expected_data() =  runBlocking<Unit>{

        mockNetworkResponseWithFileContent("success_resp_list.json", HttpURLConnection.HTTP_OK)
        mRepo = UserRepository()

        val dataReceived = mRepo.getUserProfileData(mParam)

        assertNotNull(dataReceived)
        assertEquals(dataReceived.name, mParam)
    }
}