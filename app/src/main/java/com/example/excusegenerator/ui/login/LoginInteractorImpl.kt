package com.example.excusegenerator.ui.login

import android.annotation.SuppressLint
import com.example.excusegenerator.auth.AuthInteractor
import com.example.excusegenerator.data.appPreferences.AppPreferences
import com.example.excusegenerator.requestObject.LoginRequest
import com.example.excusegenerator.service.GeneratorApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class LoginInteractorImpl : LoginInteractor {
    override lateinit var accessToken: String
    override lateinit var submittedLogin: String
    override lateinit var submittedPassword: String
    private val service = GeneratorApiService.getInstance()

    override fun login(login: String, password: String, listener: AuthInteractor.onAuthFinishedListener) {
        when{
            login.isBlank() -> listener.onLoginValueError()
            password.isBlank() -> listener.onPasswordError()
            else -> {
                loginOrError(login, password, listener)
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun loginOrError(login: String, password: String, listener: AuthInteractor.onAuthFinishedListener){
        val loginRequestObject = LoginRequest(login, password)
        submittedLogin = login
        submittedPassword = password
        service.login(loginRequestObject)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                accessToken = response.token
                listener.onAuthSuccess()
            }, { error ->
                listener.onAuthError()
                error.printStackTrace()
            })
    }

    override fun persistAccessToken(preferences: AppPreferences) {
        preferences.storeAccessToken(accessToken)
        preferences.storeUserVO(submittedLogin, submittedPassword)
    }

}