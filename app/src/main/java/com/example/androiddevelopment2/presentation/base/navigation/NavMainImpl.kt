package com.example.androiddevelopment2.presentation.base.navigation

import android.os.Bundle
import com.example.androiddevelopment2.R
import javax.inject.Inject


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