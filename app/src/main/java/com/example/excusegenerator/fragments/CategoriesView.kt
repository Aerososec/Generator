package com.example.excusegenerator.fragments

import android.content.Context

interface CategoriesView {
    fun getContext() : Context

    interface ExcuseAdapter{
        fun showExcuse(categoryName: String)
    }

}