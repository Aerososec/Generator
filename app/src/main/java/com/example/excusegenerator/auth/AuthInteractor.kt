package com.example.excusegenerator.auth


import com.example.excusegenerator.data.appPreferences.AppPreferences

interface AuthInteractor {
    var accessToken: String
    var submittedLogin: String
    var submittedPassword: String

    interface onAuthFinishedListener {
        fun onAuthSuccess()
        fun onAuthError()
        fun onLoginValueError()
        fun onPasswordError()
    }

    fun persistAccessToken(preferences: AppPreferences)
}