package com.example.excusegenerator.ui.login

interface LoginPresenter {
    fun attemptLogin(username: String, password: String)
}