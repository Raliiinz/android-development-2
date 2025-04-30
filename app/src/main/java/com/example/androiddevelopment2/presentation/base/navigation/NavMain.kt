package com.example.androiddevelopment2.presentation.base.navigation

interface NavMain {

    fun initNavMain(parent: Nav)

    fun goToSearchPage()

    fun goToDetailsPage(recipeId: Int)
}