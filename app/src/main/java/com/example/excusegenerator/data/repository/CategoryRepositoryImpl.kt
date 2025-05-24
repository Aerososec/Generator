package com.example.excusegenerator.data.repository

import android.content.Context
import com.example.excusegenerator.VO.CategoriesResponse
import com.example.excusegenerator.service.GeneratorApiService
import io.reactivex.rxjava3.core.Observable

class CategoryRepositoryImpl(context: Context) : CategoryRepository{
    val service = GeneratorApiService.getInstance()
    override fun allCategories(): Observable<CategoriesResponse> {
        return service.getAllCategories()
    }
}