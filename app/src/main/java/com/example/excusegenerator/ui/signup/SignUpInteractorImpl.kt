package com.example.excusegenerator.ui.signup

import android.annotation.SuppressLint
import com.example.excusegenerator.auth.AuthInteractor
import com.example.excusegenerator.data.appPreferences.AppPreferences
import com.example.excusegenerator.requestObject.LoginRequest
import com.example.excusegenerator.requestObject.RegistrationRequest
import com.example.excusegenerator.service.GeneratorApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers


class SignUpInteractorImpl() : SignUpInteractor {
    override lateinit var accessToken: String
    override lateinit var submittedLogin: String
    override lateinit var submittedPassword: String
    private val service = GeneratorApiService.getInstance()

    override fun persistAccessToken(preferences: AppPreferences) {
        preferences.storeAccessToken(accessToken)
    }

    override fun signUp(
        login: String,
        password: String,
        username: String,
        listener: SignUpInteractor.SignUpFinishedListener
    ) {
        when{
            username.isBlank() -> listener.onUsernameError()
            login.isBlank() -> listener.onLoginValueError()
            password.isBlank() -> listener.onPasswordError()
            else -> {
                signUpOrError(login, password, username, listener)
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun signUpOrError(login: String, password: String, username: String, listener: SignUpInteractor.SignUpFinishedListener){
        submittedPassword = password
        submittedLogin = login
        val userRequestObject = RegistrationRequest(login, password, username)
        service.registrations(userRequestObject)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                if (response.isSuccessful) {
                    listener.onSuccess()
                } else {
                    listener.onError()
                }
            }, {
                error ->
                listener.onError()
                error.printStackTrace()
            })
    }

    @SuppressLint("CheckResult")
    override fun getAuthorization(listener: AuthInteractor.onAuthFinishedListener) {
        val loginRequestObject = LoginRequest(submittedLogin, submittedPassword)
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
}