package com.example.excusegenerator.service

import com.example.excusegenerator.VO.AllExcusesResponse
import com.example.excusegenerator.VO.CategoriesResponse
import com.example.excusegenerator.VO.Excuse
import com.example.excusegenerator.VO.ExcuseResponse
import com.example.excusegenerator.VO.ListFavoritesResponse
import com.example.excusegenerator.VO.TokenVO
import com.example.excusegenerator.requestObject.LoginRequest
import com.example.excusegenerator.requestObject.PasswordRequest
import com.example.excusegenerator.requestObject.RegistrationRequest
import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface GeneratorApiService {
    companion object Factory{
        private var service : GeneratorApiService? = null

        fun getInstance(): GeneratorApiService{
            if (service == null){
                val retrofit = Retrofit.Builder()
                   // .baseUrl("http://10.0.2.2:8080") //для эмулятора
                    .baseUrl("http://192.168.138.115:8080")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build()

                service = retrofit.create(GeneratorApiService::class.java)
            }
            return service!!
        }

    }

    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest) : Observable<TokenVO>

    @POST("auth/register")
    fun registrations(@Body registrationRequest: RegistrationRequest) : Observable<Response<ResponseBody>>

    @POST("auth/logout")
    fun logout(@Header("Authorization") token : String) : Observable<Response<ResponseBody>>

    @GET("categories")
    fun getAllCategories() : Observable<CategoriesResponse>

    @GET("excuses")
    fun getAllExcuses() : Observable<AllExcusesResponse>

    @GET("excuses/random")
    fun getRandomExcuse() : Observable<Excuse>

    @GET("excuses/{categoryName}")
    fun getExcuseByCategory(@Path("categoryName") categoryName : String) : Observable<ExcuseResponse>

    @GET("favorites")
    fun getUserFavorites(@Header("Authorization") authorization: String) : Observable<Response<ListFavoritesResponse>>

    @POST("favorites/add/{excuseId}") // будем пока считать что тут текст
    fun addFavoriteExcuse(@Header("Authorization") authorization: String, @Path("excuseId") excuseId : String) : Observable<Response<ResponseBody>>

    @DELETE("favorites/{excuseId}")
    fun deleteExcuse(@Header("Authorization") authorization: String, @Path("excuseId") excuseId : String) : Observable<Response<ResponseBody>>

    @POST("auth/changePassword")
    fun changePassword(@Header("Authorization") authorization: String, @Body passwordRequest: PasswordRequest) : Observable<Response<ResponseBody>>
}