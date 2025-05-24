package com.example.excusegenerator.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.excusegenerator.R
import com.example.excusegenerator.data.appPreferences.AppPreferences
import com.example.excusegenerator.databinding.ActivitySettingsBinding
import com.example.excusegenerator.ui.login.LoginActivity

class SettingsActivity : AppCompatActivity(), SettingsView {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var presentor : SettingsPresenterImpl
    private lateinit var appPreferences : AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appPreferences = AppPreferences.create(this)
        presentor = SettingsPresenterImpl(this)
        binding.currentPassword.setText(appPreferences.password)
        binding.btnConfirm.setOnClickListener {
            attemptChangePassword(binding.newPassword.text.toString(),
                binding.currentPassword.text.toString()
            )
        }
    }

    override fun setChangePasswordError() {
        Toast.makeText(this, "Ошибка смены пароля!", Toast.LENGTH_SHORT).show()
    }

    override fun newPasswordValueError() {
        Toast.makeText(this, "Пароли должны совпадать!", Toast.LENGTH_SHORT).show()
    }


    override fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun getContext(): Context {
        return this
    }

    override fun attemptChangePassword(newPassword: String, oldPassword: String) {
        if (checkNewPassword()){
            presentor.attemptChangePassword(newPassword, oldPassword)
        }
        else{
            newPasswordValueError()
        }
    }

    private fun checkNewPassword() : Boolean{
        val attemptOne = binding.newPassword.text.toString()
        val attemptTwo = binding.confirmPassword.text.toString()
        return attemptTwo == attemptOne
    }

}