package com.example.excusegenerator.data.repository

import android.content.Context
import com.example.excusegenerator.VO.AllExcusesResponse
import com.example.excusegenerator.VO.Excuse
import com.example.excusegenerator.VO.ExcuseResponse
import com.example.excusegenerator.VO.ListFavoritesResponse
import com.example.excusegenerator.data.appPreferences.AppPreferences
import com.example.excusegenerator.service.GeneratorApiService
import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.Response

class ExcuseRepositoryImpl(val context: Context) : ExcuseRepository {
    val appPreferences = AppPreferences.create(context)
    val service = GeneratorApiService.getInstance()

    override fun getFavoritesExcuses(): Observable<Response<ListFavoritesResponse>> {
        return service.getUserFavorites("Bearer ${appPreferences.accessToken}")
    }

    override fun allExcuses(): Observable<AllExcusesResponse> {
        TODO("Not yet implemented")
        // не знаю зачем
    }


    override fun excuseByCategory(categoryName: String): Observable<ExcuseResponse> {
        return service.getExcuseByCategory(categoryName)
    }

    override fun addExcuseToFavorite(excuseId: String) : Observable<Response<ResponseBody>> {
        return service.addFavoriteExcuse("Bearer ${appPreferences.accessToken}", excuseId)
    }

    override fun deleteExcuse(excuseId: String) : Observable<Response<ResponseBody>> {
        return service.deleteExcuse("Bearer ${appPreferences.accessToken}", excuseId)
    }
}