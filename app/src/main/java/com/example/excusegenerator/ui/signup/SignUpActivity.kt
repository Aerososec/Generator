package com.example.excusegenerator.ui.signup

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
import com.example.excusegenerator.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity(), SignUpView {
    lateinit var binding: ActivitySignUpBinding
    lateinit var presenter :  SignUpPresenterImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        presenter = SignUpPresenterImpl(this)

        binding.btnSignUp.setOnClickListener {
            presenter.attemptRegistration(
                binding.forLoginSign.text.toString(),
                binding.forPasswordSign.text.toString(),
                binding.forUsernameSign.text.toString(),

            )
        }
    }

    override fun showProgress() {
        binding.progressBarReg.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.progressBarReg.visibility = View.GONE
    }

    override fun showSignUpError() {
        Toast.makeText(this, "Sign up Error", Toast.LENGTH_LONG).show()
    }

    override fun setUsernameError() {
        binding.forUsernameSign.error = "Username field cannot be empty!"
    }

    override fun setPasswordError() {
        binding.forPasswordSign.error = "Password field cannot be empty!"
    }

    override fun setLoginValueError() {
        binding.forLoginSign.error = "Login field cannot be empty!"
    }

    override fun navigateToHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun getContext(): Context {
        return this
    }

    override fun showAuthError() {
        Toast.makeText(this, "Auth Error", Toast.LENGTH_LONG).show()
    }
}