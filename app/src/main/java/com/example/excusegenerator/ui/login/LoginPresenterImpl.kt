package com.example.excusegenerator.ui.login

import com.example.excusegenerator.auth.AuthInteractor
import com.example.excusegenerator.data.appPreferences.AppPreferences

class LoginPresenterImpl(private val view: LoginView) : LoginPresenter, AuthInteractor.onAuthFinishedListener{
    private val loginInteractorImpl = LoginInteractorImpl()
    private val appPreferences = AppPreferences.create(view.getContext())

    override fun attemptLogin(username: String, password: String) {
        view.showProgress()
        loginInteractorImpl.login(username, password, this)
    }

    override fun onAuthSuccess() {
        loginInteractorImpl.persistAccessToken(appPreferences)
        view.hideProgress()
        view.navigateToHome()
    }

    override fun onAuthError() {
        view.hideProgress()
        view.showAuthError()
    }

    override fun onLoginValueError() {
        view.hideProgress()
        view.setLoginValueError()
    }

    override fun onPasswordError() {
        view.hideProgress()
        view.setPasswordError()
    }

}