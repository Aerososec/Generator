package com.example.excusegenerator.data.repository

import com.example.excusegenerator.VO.AllExcusesResponse
import com.example.excusegenerator.VO.Excuse
import com.example.excusegenerator.VO.ExcuseResponse
import com.example.excusegenerator.VO.ListFavoritesResponse
import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.Response


interface ExcuseRepository {
    fun getFavoritesExcuses() : Observable<Response<ListFavoritesResponse>>
    fun allExcuses() : Observable<AllExcusesResponse>
    fun excuseByCategory(categoryName : String) : Observable<ExcuseResponse>
    fun addExcuseToFavorite(excuseId: String) : Observable<Response<ResponseBody>>
    fun deleteExcuse(excuseId: String) : Observable<Response<ResponseBody>>
}
