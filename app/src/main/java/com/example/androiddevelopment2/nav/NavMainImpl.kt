package com.example.androiddevelopment2.nav

import android.os.Bundle
import com.example.androiddevelopment2.app.R
import com.example.androiddevelopment2.navigation.Nav
import com.example.androiddevelopment2.navigation.NavMain
import javax.inject.Inject
import com.example.androiddevelopment2.base.R as baseR

class NavMainImpl @Inject constructor(
    private val navigatorDelegate: NavigatorDelegate,
) : NavMain {

    private var parent: Nav? = null

    override fun initNavMain(parent: Nav) {
        this.parent = parent
    }

    override fun goToSearchPage() {
        navigatorDelegate.navigate(action = R.id.action_global_search_fragment)
    }

    override fun goToDetailsPage(recipeId: Int) {
        val args = Bundle().apply {
            putInt("recipeId", recipeId)
        }
        navigatorDelegate.navigate(
            action = R.id.action_global_recipe_details_fragment,
            args = args
        )
    }
}
