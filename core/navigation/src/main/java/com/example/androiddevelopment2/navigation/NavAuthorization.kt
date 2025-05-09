package com.example.androiddevelopment2.navigation

interface NavAuthorization {

    fun initNavMain(parent: Nav)

    fun goToRegisterPage()

    fun goToAuthPage()

    fun goToMainPage(userId: String)
}