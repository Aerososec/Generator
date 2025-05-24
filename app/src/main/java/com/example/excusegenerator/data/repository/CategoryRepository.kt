package com.example.excusegenerator.data.repository

import com.example.excusegenerator.VO.CategoriesResponse
import io.reactivex.rxjava3.core.Observable

interface CategoryRepository {
    fun allCategories() : Observable<CategoriesResponse>
}