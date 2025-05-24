package com.example.excusegenerator.main

interface MainPresentor{
    fun loadCategories()
    fun loadFavorites()
    fun loadExcuse(categoryName: String)
    fun attemptLogOut()
    fun attemptDeleteFavorite(excuseId: String)
    fun addExcuseToFavorite(excuseId: String)
}