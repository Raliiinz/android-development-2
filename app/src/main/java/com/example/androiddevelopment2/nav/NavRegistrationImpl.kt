package com.example.androiddevelopment2.nav

import com.example.androiddevelopment2.app.R
import com.example.androiddevelopment2.navigation.Nav
import com.example.androiddevelopment2.navigation.NavRegistration
import javax.inject.Inject

class NavRegistrationImpl @Inject constructor(
    private val navigatorDelegate: NavigatorDelegate,
) : NavRegistration {

    private var parent: Nav? = null

    override fun initNavMain(parent: Nav) {
        this.parent = parent
    }

    override fun goToAuthPage() {
        navigatorDelegate.navigate(action = R.id.action_global_auth)
    }

}
