package com.example.androiddevelopment2.presentation.base.navigation

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavOptions
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton
import androidx.navigation.Navigator

@Singleton
class NavigatorDelegate @Inject constructor() {

    private var navProvider: WeakReference<Nav.Provider>? = null

    fun setNavProvider(navProvider: Nav.Provider) {
        this.navProvider = WeakReference(navProvider)
    }

    fun clearNavProvider(navProvider: Nav.Provider) {
        if (this.navProvider?.get() == navProvider) {
            this.navProvider = null
        }
    }

    fun navigate(
        @IdRes action: Int,
        args: Bundle? = null,
        options: NavOptions? = null,
        extras: Navigator.Extras? = null
    ) {
        navProvider?.get()?.getNavController()?.navigate(action, args, options, extras)
    }
}