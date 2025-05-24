package com.example.excusegenerator.ui.signup

import com.example.excusegenerator.auth.AuthInteractor
import com.example.excusegenerator.data.appPreferences.AppPreferences


class SignUpPresenterImpl(private val view: SignUpView) : SingUpPresenter, SignUpInteractor.SignUpFinishedListener, AuthInteractor.onAuthFinishedListener{
    val appPreferences = AppPreferences.create(view.getContext())
    private val signUpInteractorImpl = SignUpInteractorImpl()

    override fun attemptRegistration(
        login: String,
        password: String,
        username: String
    ) {
        view.showProgress()
        signUpInteractorImpl.signUp(login, password, username, this)
    }

    override fun onSuccess() {
        signUpInteractorImpl.getAuthorization(this)
        //view.navigateToHome()
    }

    override fun onLoginValueError() {
        view.hideProgress()
        view.setLoginValueError()
    }


    override fun onPasswordError() {
        view.hideProgress()
        view.setPasswordError()
    }

    override fun onError() {
        view.hideProgress()
        view.showSignUpError()
    }

    override fun onUsernameError() {
        view.hideProgress()
        view.setUsernameError()
    }

    override fun onAuthSuccess() {
        signUpInteractorImpl.persistAccessToken(appPreferences)
        view.showProgress()
        view.navigateToHome()
    }

    override fun onAuthError() {
        view.hideProgress()
        view.showAuthError()
    }


}