package com.example.excusegenerator.ui.signup

import android.content.Context
import com.example.excusegenerator.ui.login.AuthView


interface SignUpView : AuthView {
    fun showProgress()
    fun hideProgress()
    fun showSignUpError()
    fun setLoginValueError()
    fun setUsernameError()
    fun setPasswordError()
    fun navigateToHome()
    fun getContext() : Context
}