package com.example.excusegenerator.ui.signup

import com.example.excusegenerator.auth.AuthInteractor


interface SignUpInteractor : AuthInteractor {
    interface SignUpFinishedListener{
        fun onSuccess()
        fun onLoginValueError()
        fun onPasswordError()
        fun onError()
        fun onUsernameError()
    }

    fun signUp(username : String, login: String, password: String, listener: SignUpFinishedListener)
    fun getAuthorization(listener: AuthInteractor.onAuthFinishedListener)
}