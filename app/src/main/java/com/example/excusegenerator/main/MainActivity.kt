package com.example.excusegenerator.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.excusegenerator.R
import com.example.excusegenerator.databinding.ActivityMainBinding
import com.example.excusegenerator.fragments.ExcuseDetailsFragment
import com.example.excusegenerator.fragments.FavoritesFragment
import com.example.excusegenerator.fragments.MainFragment
import com.example.excusegenerator.ui.login.LoginActivity
import com.example.excusegenerator.ui.settings.SettingsActivity

class MainActivity : AppCompatActivity(), MainView {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainPresenter : MainPresentorImpl
    private val categoryFragment = MainFragment()
    private val favoritesFragment = FavoritesFragment()
    private val excuseFragment = ExcuseDetailsFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        mainPresenter = MainPresentorImpl(this)
        showCategoryScreen()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                showCategoryScreen()
                return true
            }
            R.id.action_logout -> {
                mainPresenter.attemptLogOut()
                return true
            }
            R.id.settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getContext(): Context {
        return this
    }

    override fun showCategoryError() {
        Toast.makeText(this, "Category Error", Toast.LENGTH_SHORT).show()
    }

    override fun showFavoritesError() {
        Toast.makeText(this, "Favorites error", Toast.LENGTH_SHORT).show()
    }

    override fun showLogoutError() {
        Toast.makeText(this, "Logout error", Toast.LENGTH_SHORT).show()
    }

    override fun showCategoryScreen(){
        supportFragmentManager.beginTransaction().replace(binding.container.id, categoryFragment).commit()
        mainPresenter.loadCategories()
        supportActionBar?.title = "Categories"
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun showFavoritesScreen(){
        supportFragmentManager.beginTransaction().replace(binding.container.id, favoritesFragment).commit()
        supportActionBar?.title = "Favorites"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun loadFavorites(){
        mainPresenter.loadFavorites()
    }


    override fun getCategoryFragment(): MainFragment {
        return categoryFragment
    }

    override fun getFavoritesFragment(): FavoritesFragment{
        return favoritesFragment
    }

    override fun getExcuseFragment(): ExcuseDetailsFragment {
        return excuseFragment
    }

    override fun showNoFavorites() {
        Toast.makeText(this, "No favorites", Toast.LENGTH_SHORT).show()
    }

    override fun showNoCategories() {
        Toast.makeText(this, "No categories", Toast.LENGTH_SHORT).show()
    }

    override fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun showExcuseByCategory(categoryName: String) {
        supportFragmentManager.beginTransaction().replace(binding.container.id, excuseFragment).commit()
        mainPresenter.loadExcuse(categoryName)
        supportActionBar?.title = "Excuse by category"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun deleteFavorite(excuseId: String) {
        mainPresenter.attemptDeleteFavorite(excuseId)
    }

    override fun deleteSuccess(){
        Toast.makeText(this, "Delete success", Toast.LENGTH_SHORT).show()
    }

    override fun deleteError(){
        Toast.makeText(this, "Delete error", Toast.LENGTH_SHORT).show()
    }

    override fun addExcuseToFavorite(excuseId : String) {
        mainPresenter.addExcuseToFavorite(excuseId)
    }

    override fun addSuccess() {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
    }

    override fun addError() {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
    }

}