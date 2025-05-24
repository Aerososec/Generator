package com.example.excusegenerator.ui.settings

import android.content.Context
import android.content.Intent
import com.example.excusegenerator.ui.login.LoginActivity

class SettingsPresenterImpl(val view: SettingsView) : SettingsPresenter, SettingsInteractor.ChangePasswordFinish {
    val interactorImpl = SettingsInteractorImpl(view.getContext())

    override fun attemptChangePassword(newPassword: String, oldPassword: String) {
        interactorImpl.changePassword(newPassword, oldPassword, this)
    }

    override fun changeSuccess(){
        view.navigateToLogin()
    }

    override fun changeError() {
        view.setChangePasswordError()
    }

}