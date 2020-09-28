package com.example.usergithubrepos.view

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.example.usergithubrepos.R
import com.example.usergithubrepos.data.UserUseCase
import com.example.usergithubrepos.data.UserViewModel
import com.example.usergithubrepos.models.userprofile.ProfileResponse
import com.example.usergithubrepos.service.LiveDataWrapper
import com.example.usergithubrepos.utils.BaseViewModelFactory
import com.example.usergithubrepos.utils.hideKeyboard
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val mUserUseCase : UserUseCase by inject()
    private val mBaseViewModelFactory : BaseViewModelFactory = BaseViewModelFactory(Dispatchers.Main, Dispatchers.IO, mUserUseCase)
    private val  TAG = MainActivity::class.simpleName
    private val mViewModel : UserViewModel by lazy {
        ViewModelProviders.of(this, mBaseViewModelFactory).get(UserViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setEditTextListener()
        observeData()
        button_repo.setOnClickListener {
            startActivity(Intent(this, ReposListActivity::class.java)
                .putExtra("data", edit_username.text.toString().trim()))
        }
    }

    private fun observeData() {
        //Start observing the targets
        mViewModel.mUserData.observe(this, mDataObserver)
    }

    private val mDataObserver = Observer<LiveDataWrapper<ProfileResponse>>{
        when (it?.responseStatus) {
            LiveDataWrapper.RESPONSESTATUS.LOADING -> {
                changeProgressBarVisibility(true)
            }

            LiveDataWrapper.RESPONSESTATUS.ERROR -> {
                // Error for http request
                Log.e(TAG,"LiveDataResult.Status.ERROR = ${it.response}")
                changeVisibilityOnError(true)
                changeProgressBarVisibility(false)
            }

            LiveDataWrapper.RESPONSESTATUS.SUCCESS -> {
                // Data from API
                Log.d(TAG,"LiveDataResult.Status.SUCCESS = ${it.response}")
                changeProgressBarVisibility(false)
                changeVisibilityOnError(false)
                setData(it.response)
            }
        }
    }

    private fun setData(profileResponse: ProfileResponse?) {
        profileResponse?.let {

            if (!profileResponse.avatar_url.isNullOrEmpty())
               Picasso.get().load(profileResponse.avatar_url).into(image_profile)

            text_username.text = profileResponse?.name.capitalize()
            text_bio.text = profileResponse?.bio?.capitalize()
            text_location.text = profileResponse?.location?.capitalize()

            text_followers.text = "followers : " + profileResponse?.followers
            text_following.text = "following : " + profileResponse?.following

            text_repos.text = "Public repos : " + profileResponse?.public_repos

            if (profileResponse.public_repos > 0) {
                button_repo.visibility = View.VISIBLE
            } else {
                button_repo.visibility = View.GONE
            }
        }
    }

    private fun setEditTextListener() {
        edit_username.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (!TextUtils.isEmpty(edit_username.text.toString().trim())) {
                    hideKeyboard(this)
                    mViewModel.fetchUserProfile(edit_username.text.toString().trim())
                    return@OnEditorActionListener true
                }
            }
            false
        })
    }

    private fun changeProgressBarVisibility(visible: Boolean) {
        progress_circular.visibility = if (visible) View.VISIBLE else View.GONE
    }

   private fun changeVisibilityOnError(visible: Boolean) {
        group_divider.visibility = if (visible) View.GONE else View.VISIBLE
        text_error.visibility = if (visible) View.VISIBLE else View.GONE
        button_repo.visibility = if (visible) View.GONE else View.VISIBLE
    }
}