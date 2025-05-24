package com.example.excusegenerator.main

import android.content.Context
import com.example.excusegenerator.fragments.ExcuseDetailsFragment
import com.example.excusegenerator.fragments.FavoritesFragment
import com.example.excusegenerator.fragments.MainFragment


interface MainView {
    fun getContext() : Context
    fun showCategoryError()
    fun showFavoritesError()
    fun showLogoutError()
    fun showCategoryScreen()
    fun showFavoritesScreen()
    fun getCategoryFragment() : MainFragment
    fun getFavoritesFragment() : FavoritesFragment
    fun getExcuseFragment() : ExcuseDetailsFragment
    fun showNoFavorites()
    fun showNoCategories()
    fun navigateToLogin()
    fun showExcuseByCategory(categoryName: String)
    fun deleteFavorite(excuseId : String)
    fun deleteSuccess()
    fun deleteError()
    fun addExcuseToFavorite(excuse: String)
    fun addSuccess()
    fun addError()
}