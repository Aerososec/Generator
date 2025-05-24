package com.example.excusegenerator.ui.settings

import android.content.Context

interface SettingsView {
    fun setChangePasswordError()
    fun newPasswordValueError()
    fun navigateToLogin()
    fun getContext() : Context
    fun attemptChangePassword(newPassword: String, oldPassword: String)
}