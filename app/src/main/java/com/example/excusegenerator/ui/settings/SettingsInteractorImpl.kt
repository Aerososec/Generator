package com.example.excusegenerator.ui.settings

import android.annotation.SuppressLint
import android.content.Context
import com.example.excusegenerator.data.appPreferences.AppPreferences
import com.example.excusegenerator.requestObject.PasswordRequest
import com.example.excusegenerator.service.GeneratorApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class SettingsInteractorImpl(context: Context) : SettingsInteractor {
    val appPreferences = AppPreferences.create(context)
    val service = GeneratorApiService.getInstance()

    override fun persistNewPassword(newPassword: String) {
        appPreferences.storeNewPassword(newPassword)
    }

    @SuppressLint("CheckResult")
    override fun changePassword(
        newPassword: String,
        oldPassword: String,
        listener: SettingsInteractor.ChangePasswordFinish
    ) {
        val request = PasswordRequest(newPassword, oldPassword)
        service.changePassword("Bearer ${appPreferences.accessToken}", request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                        res ->
                    if (res.isSuccessful){
                        persistNewPassword(newPassword)
                        listener.changeSuccess()
                    }
                    else{
                        listener.changeError()
                    }

                },
                {
                        error ->
                    listener.changeError()
                    error.printStackTrace()
                }
            )
    }

}