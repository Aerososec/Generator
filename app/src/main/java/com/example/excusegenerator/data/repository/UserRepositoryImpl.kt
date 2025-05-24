package com.example.excusegenerator.data.repository

import android.content.Context
import com.example.excusegenerator.data.appPreferences.AppPreferences
import com.example.excusegenerator.service.GeneratorApiService
import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.Response

class UserRepositoryImpl(val context: Context) : UserRepository {
    val service = GeneratorApiService.getInstance()
    val appPreferences = AppPreferences.create(context)

    override fun logout(): Observable<Response<ResponseBody>> {
        return service.logout("Bearer ${appPreferences.accessToken}")
    }
}