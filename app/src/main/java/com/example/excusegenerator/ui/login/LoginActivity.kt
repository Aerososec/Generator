package com.example.excusegenerator.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.excusegenerator.main.MainActivity
import com.example.excusegenerator.R
import com.example.excusegenerator.data.appPreferences.AppPreferences
import com.example.excusegenerator.databinding.ActivityLoginBinding
import com.example.excusegenerator.ui.signup.SignUpActivity

class LoginActivity : AppCompatActivity(), LoginView {
    lateinit var binding: ActivityLoginBinding
    private val loginValueError = "Username field cannot be empty!"
    private val passwordError = "Password field cannot be empty!"
    private lateinit var appPreferences: AppPreferences
    private lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        presenter = LoginPresenterImpl(this)

        attemptLoginPreferences()

        binding.btnLogin.setOnClickListener {
            presenter.attemptLogin(binding.forLoginValue.text.toString(), binding.forPassword.text.toString())
        }

        binding.btnRegistrations.setOnClickListener {
            navigateToSignUp()
        }

    }

    fun attemptLoginPreferences(){
        appPreferences = AppPreferences.create(this)
        val login = appPreferences.login
        val password = appPreferences.password
        if (login != null && password != null){
            binding.forLoginValue.setText(login)
            binding.forPassword.setText(password)
        }
    }

    override fun showProgress() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.progressBar.visibility = View.GONE
    }

    override fun setLoginValueError() {
        binding.forLoginValue.error = loginValueError
    }

    override fun setPasswordError() {
        binding.forPassword.error = passwordError
    }


    override fun navigateToSignUp() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    override fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun getContext() : Context{
        return this
    }

    override fun showAuthError() {
        Toast.makeText(this, "Login or password are invalid!", Toast.LENGTH_SHORT).show()
    }

}