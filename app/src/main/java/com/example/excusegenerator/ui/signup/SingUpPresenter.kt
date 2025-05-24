package com.example.excusegenerator.ui.signup

interface SingUpPresenter {
    fun attemptRegistration(username: String, login: String, password: String)
}