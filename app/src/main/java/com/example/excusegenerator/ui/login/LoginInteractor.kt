package com.example.excusegenerator.ui.login

import com.example.excusegenerator.auth.AuthInteractor
import com.example.excusegenerator.data.appPreferences.AppPreferences

interface LoginInteractor : AuthInteractor {

    fun login(
        username: String,
        password: String,
        listener: AuthInteractor.onAuthFinishedListener
    )

}