package com.example.excusegenerator.requestObject

data class LoginRequest(val login: String, val password: String)

data class RegistrationRequest(val login: String, val password: String, val username: String)

data class PasswordRequest(val newPassword: String, val oldPassword: String)