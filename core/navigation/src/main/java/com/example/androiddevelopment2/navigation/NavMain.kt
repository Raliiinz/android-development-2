package com.example.androiddevelopment2.navigation

interface NavMain {

    fun initNavMain(parent: Nav)

    fun goToSearchPage()

    fun goToDetailsPage(recipeId: Int)

    fun goToGraphPage()
}