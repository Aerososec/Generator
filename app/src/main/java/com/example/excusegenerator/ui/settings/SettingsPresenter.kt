package com.example.excusegenerator.ui.settings

interface SettingsPresenter {
    fun attemptChangePassword(newPassword: String, oldPassword: String)
}