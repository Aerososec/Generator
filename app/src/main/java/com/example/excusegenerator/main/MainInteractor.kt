package com.example.excusegenerator.main

import com.example.excusegenerator.VO.Excuse
import com.example.excusegenerator.VO.ExcuseResponse
import com.example.excusegenerator.VO.ListFavoritesResponse


interface MainInteractor{
    interface CategoryLoadFinish{
        fun categoryLoadSuccess(categories: List<String>)
        fun categoryLoadError()
    }

    interface FavoritesLoadFinish{
        fun favoritesLoadSuccess(favorites:  ListFavoritesResponse)
        fun favoritesLoadError()
    }

    interface ExcuseLoadFinish{
        fun excuseLoadSuccess(excuse: ExcuseResponse)
        fun excuseLoadError()
    }

    interface LogOutFinish{
        fun logOutSuccess()
        fun logoutError()
    }

    interface DeleteFavoriteFinish{
        fun deleteSuccess()
        fun deleteError()
    }

    interface AddExcuseFinish{
        fun addSuccess()
        fun addError()
    }

    fun categoryLoad(listener: CategoryLoadFinish)
    fun favoritesLoad(listener: FavoritesLoadFinish)
    fun excuseLoad(listener: ExcuseLoadFinish, categoryName: String)
    fun logOut(listener: LogOutFinish)
    fun deleteFavorite(listener: DeleteFavoriteFinish, excuseId: String)
    fun addExcuseToFavorite(listener: AddExcuseFinish, excuseId: String)
}