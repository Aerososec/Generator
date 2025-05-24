package com.example.excusegenerator.ui.settings

interface SettingsInteractor {

    interface ChangePasswordFinish{
        fun changeSuccess()
        fun changeError()
    }

    fun persistNewPassword(newPassword: String)
    fun changePassword(newPassword: String, oldPassword: String, listener:ChangePasswordFinish )
}