package com.example.excusegenerator.VO

data class TokenVO(val value: String, val token: String)

data class CategoryVO(val value: String)

data class ExcuseVO(val value: String)

data class CategoriesResponse(val value: List<String>)

data class AllExcusesResponse(
    val value: List<Excuse>
)

data class ExcuseResponse(
    val value: List<Excuse>
)

data class ListFavoritesResponse(
    val value: List<FavoritesResponse>
)

data class FavoritesResponse(
    val UserID: String,
    val ExcuseID: Int,
    val Excuse: Excuse
)

data class Excuse(
    val ID: Int,
    val Text: String,
    val CategoryID: Int,
    val Category: Category,
    val Plausibility: Int
)

data class Category(
    val ID: Int,
    val CategoryName: String
)