package com.example.excusegenerator.main


import android.annotation.SuppressLint
import com.example.excusegenerator.VO.ExcuseResponse
import com.example.excusegenerator.VO.ListFavoritesResponse
import com.example.excusegenerator.data.appPreferences.AppPreferences

class MainPresentorImpl(val view: MainView) : MainPresentor, MainInteractor.LogOutFinish, MainInteractor.CategoryLoadFinish, MainInteractor.FavoritesLoadFinish, MainInteractor.ExcuseLoadFinish, MainInteractor.DeleteFavoriteFinish, MainInteractor.AddExcuseFinish {
    private val interactorImpl = MainInteractorImpl(view.getContext())

    override fun logOutSuccess() {
        val appPreferences = AppPreferences.create(view.getContext())
        appPreferences.clear()
        view.navigateToLogin()
    }

    override fun logoutError() {
        view.showLogoutError()
    }

    override fun loadCategories() {
        interactorImpl.categoryLoad(this)
    }

    override fun loadFavorites() {
        interactorImpl.favoritesLoad(this)
    }

    override fun loadExcuse(categoryName: String) {
        interactorImpl.excuseLoad(this, categoryName)
    }

    override fun attemptLogOut() {
        interactorImpl.logOut(this)
    }

    override fun attemptDeleteFavorite(excuseId: String) {
        interactorImpl.deleteFavorite(this, excuseId)
    }

    override fun addExcuseToFavorite(excuseId: String) {
        interactorImpl.addExcuseToFavorite(this, excuseId)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun categoryLoadSuccess(allCategories: List<String>) {
        if (allCategories.isNotEmpty()){
            val mainFragment = view.getCategoryFragment()
            val categories = mainFragment.categories
            categories.clear()
            val categoryAdapter = mainFragment.mainFragmentAdapter
            categories.addAll(allCategories)
            categoryAdapter.notifyDataSetChanged()
        }
        else{
            view.showNoCategories()
        }
    }

    override fun categoryLoadError() {
        view.showCategoryError()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun favoritesLoadSuccess(getFavorites: ListFavoritesResponse) {
        if (getFavorites.value.isNotEmpty()){
            val mainFragment = view.getFavoritesFragment()
            val favorites = mainFragment.excuses
            favorites.clear()
            val conversationAdapter = mainFragment.favoritesAdapter
            favorites.addAll(getFavorites.value.map { it.Excuse })
            conversationAdapter?.notifyDataSetChanged()
        }
        else{
            view.showNoFavorites()
        }
    }

    override fun favoritesLoadError() {
        view.showFavoritesError()
    }

    override fun excuseLoadSuccess(getExcuse:  ExcuseResponse){
        val excuseFragment = view.getExcuseFragment()
        val excuses = excuseFragment.excuses
        excuses.clear()
        getExcuse.value.forEach { excuse ->
            excuses.add(excuse)
        }
    }

    override fun excuseLoadError() {
        val excuseFragment = view.getExcuseFragment()
        excuseFragment.binding.excuseTextView.text = "Ошибка"
    }

    override fun deleteSuccess() {
        view.deleteSuccess()
    }

    override fun deleteError() {
        view.deleteError()
    }

    override fun addSuccess() {
        view.addSuccess()
    }

    override fun addError() {
        view.addError()
    }
}