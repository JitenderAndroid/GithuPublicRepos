package com.example.usergithubrepos.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.usergithubrepos.R
import com.example.usergithubrepos.data.UserUseCase
import com.example.usergithubrepos.data.UserViewModel
import com.example.usergithubrepos.models.repos.UserReposResponse
import com.example.usergithubrepos.service.LiveDataWrapper
import com.example.usergithubrepos.utils.BaseViewModelFactory
import com.example.usergithubrepos.view.adapter.RecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_repos_list.*
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.android.inject

class ReposListActivity : AppCompatActivity() {

    private val mUserUseCase : UserUseCase by inject()
    private val mBaseViewModelFactory : BaseViewModelFactory = BaseViewModelFactory(Dispatchers.Main, Dispatchers.IO, mUserUseCase)
    private val  TAG = MainActivity::class.simpleName
    private var userName: String = ""
    private val mViewModel : UserViewModel by lazy {
        ViewModelProviders.of(this, mBaseViewModelFactory).get(UserViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repos_list)

        getIntentData()
        observeData()
    }

    private fun getIntentData() {
       intent.extras?.getString("data")?.let {
           userName = it
       }
    }

    private fun observeData() {
        //Start observing the targets
        mViewModel.mUserReposList.observe(this, mDataObserver)
        mViewModel.fetchUserRepos(userName)
    }

    private val mDataObserver = Observer<LiveDataWrapper<List<UserReposResponse>>>{
        when (it?.responseStatus) {
            LiveDataWrapper.RESPONSESTATUS.LOADING -> {
                changeProgressBarVisibility(true)
            }

            LiveDataWrapper.RESPONSESTATUS.ERROR -> {
                // Error for http request
                Log.e(TAG,"LiveDataResult.Status.ERROR = ${it.response}")
                //changeVisibilityOnError(true)
                changeProgressBarVisibility(false)
            }

            LiveDataWrapper.RESPONSESTATUS.SUCCESS -> {
                // Data from API
                Log.d(TAG,"LiveDataResult.Status.SUCCESS = ${it.response}")
                changeProgressBarVisibility(false)
                setData(it.response as ArrayList<UserReposResponse?>)
            }
        }
    }


    private fun setData(data : ArrayList<UserReposResponse?>) {
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        recycler_view.adapter = RecyclerViewAdapter(this, data)
    }

    private fun changeProgressBarVisibility(visible: Boolean) {
        progress_circular.visibility = if (visible) View.VISIBLE else View.GONE
    }
}