package com.example.androiddevelopment2.nav

import com.example.androiddevelopment2.navigation.Nav
import com.example.androiddevelopment2.navigation.NavMain
import javax.inject.Inject

class NavImpl @Inject constructor(
    private val navigatorDelegate: NavigatorDelegate,
    private val navMain: NavMain,
    private val appNavigator: AppNavigator
) : Nav, NavMain by navMain {

    init {
        initNavMain(parent = this)
    }

    override fun setNavProvider(navProvider: Nav.Provider) {
        navigatorDelegate.setNavProvider(navProvider = navProvider)
    }

    override fun clearNavProvider(navProvider: Nav.Provider) {
        navigatorDelegate.clearNavProvider(navProvider = navProvider)
    }

    override fun goToScreen(screen: String) {
        appNavigator.navigateTo(screen)
    }
}
