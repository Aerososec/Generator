package com.example.excusegenerator.ui.login

import android.content.Context

interface LoginView : AuthView {
    fun showProgress()
    fun hideProgress()
    fun setLoginValueError()
    fun setPasswordError()
    fun navigateToSignUp()
    fun navigateToHome()
    fun getContext() : Context
}