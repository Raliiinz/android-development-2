package com.example.androiddevelopment2.nav

import android.os.Bundle
import com.example.androiddevelopment2.app.R
import com.example.androiddevelopment2.navigation.Nav
import com.example.androiddevelopment2.navigation.NavAuthorization
import javax.inject.Inject

class NavAuthorizationImpl @Inject constructor(
    private val navigatorDelegate: NavigatorDelegate,
) : NavAuthorization {

    private var parent: Nav? = null

    override fun initNavMain(parent: Nav) {
        this.parent = parent
    }

    override fun goToRegisterPage() {
        navigatorDelegate.navigate(action = R.id.action_global_register)
    }

    override fun goToAuthPage() {
        navigatorDelegate.navigate(action = R.id.action_global_auth)
    }

    override fun goToMainPage(userId: String) {
        val args = Bundle().apply {
            putString("userId", userId)
        }
        navigatorDelegate.navigate(
            action = R.id.action_global_search,
            args = args
        )
    }
}
