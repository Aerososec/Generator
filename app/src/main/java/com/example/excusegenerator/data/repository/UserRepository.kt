package com.example.excusegenerator.data.repository

import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.Response

interface UserRepository {
    fun logout() : Observable<Response<ResponseBody>>
}