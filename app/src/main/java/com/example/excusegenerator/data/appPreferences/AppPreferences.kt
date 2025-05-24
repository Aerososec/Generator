package com.example.excusegenerator.data.appPreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit


private const val ACCESS_TOKEN = "ACCESS_TOKEN"

class AppPreferences private constructor(){
    private lateinit var preferences: SharedPreferences

    companion object{
        private const val PREFERENCES_FILE_NAME = "APP_PREFERENCES_EXCUSES"
        fun create(context: Context) : AppPreferences{
            val appPreferences = AppPreferences()
            appPreferences.preferences = context.getSharedPreferences(PREFERENCES_FILE_NAME, 0)
            return appPreferences
        }
    }

    val accessToken : String?
        get() = preferences.getString(ACCESS_TOKEN, null)
    fun storeAccessToken(accessToken : String){
        preferences.edit() { putString(ACCESS_TOKEN, accessToken) }
    }


    val login : String?
        get() = preferences.getString("LOGIN", null)

    val password : String?
        get() = preferences.getString("PASSWORD", null)

    fun storeUserVO(login: String, password: String){
        val editor = preferences.edit()
        editor.putString("LOGIN", login).apply()
        editor.putString("PASSWORD", password).apply()
    }

    fun clear(){
        preferences.edit() { clear() }
    }

    fun storeNewPassword(newPassword: String){
        preferences.edit() {
            putString("PASSWORD", newPassword)
        }
    }
}