package com.example.excusegenerator.main

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.excusegenerator.data.repository.CategoryRepositoryImpl
import com.example.excusegenerator.data.repository.ExcuseRepositoryImpl
import com.example.excusegenerator.data.repository.UserRepository
import com.example.excusegenerator.data.repository.UserRepositoryImpl
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException

class MainInteractorImpl(val context: Context) : MainInteractor {
    val categoryRepository = CategoryRepositoryImpl(context)
    val excuseRepository = ExcuseRepositoryImpl(context)
    val userRepository = UserRepositoryImpl(context)

    @SuppressLint("CheckResult")
    override fun categoryLoad(listener: MainInteractor.CategoryLoadFinish) {
        categoryRepository.allCategories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                        res ->
                    listener.categoryLoadSuccess(res.value)
                },
                {
                        error ->
                    listener.categoryLoadError()
                    error.printStackTrace()
                }
            )
    }

    @SuppressLint("CheckResult")
    override fun favoritesLoad(listener: MainInteractor.FavoritesLoadFinish) {
        excuseRepository.getFavoritesExcuses()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                        res ->
                    if (res.isSuccessful){
                        listener.favoritesLoadSuccess(res.body()!!)
                    }
                },
                {
                        error ->
                    listener.favoritesLoadError()
                    error.printStackTrace()
                }
            )
    }

    @SuppressLint("CheckResult")
    override fun excuseLoad(listener: MainInteractor.ExcuseLoadFinish, categoryName: String) {
        excuseRepository.excuseByCategory(categoryName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                        res ->
                    listener.excuseLoadSuccess(res)
                },
                {
                        error ->
                    listener.excuseLoadError()
                    error.printStackTrace()
                }
            )
    }

    @SuppressLint("CheckResult")
    override fun logOut(listener: MainInteractor.LogOutFinish) {
        userRepository.logout()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                        res ->
                    if (res.isSuccessful)
                        listener.logOutSuccess()
                    else
                        listener.logoutError()
                },
                {
                        error ->
                    listener.logoutError()
                    error.printStackTrace()
                }
            )
    }

    @SuppressLint("CheckResult")
    override fun deleteFavorite(listener: MainInteractor.DeleteFavoriteFinish, excuseId: String) {
        excuseRepository.deleteExcuse(excuseId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                        res ->
                    listener.deleteSuccess()
                },
                {
                        error ->
                    listener.deleteError()
                    error.printStackTrace()
                }
            )
    }

    @SuppressLint("CheckResult")
    override fun addExcuseToFavorite(
        listener: MainInteractor.AddExcuseFinish,
        excuseId: String
    ) {
        excuseRepository.addExcuseToFavorite(excuseId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                        res ->
                    listener.addSuccess()
                },
                {
                        error ->
                    listener.addError()
                    error.printStackTrace()
                }
            )
    }
}