package com.example.androiddevelopment2

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.androiddevelopment2.app.R
import com.example.androiddevelopment2.authorization.AuthorizationViewModel
import com.example.androiddevelopment2.domain.firebase.fcm.model.ScreenDestination
import com.example.androiddevelopment2.domain.firebase.fcm.repository.FcmRepository
import com.example.androiddevelopment2.domain.usecase.IsUserAuthorizedUseCase
import com.example.androiddevelopment2.navigation.Nav
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.getValue
import com.example.androiddevelopment2.authorization.R as AuthR
import com.example.androiddevelopment2.search.R as SearchR
import com.example.androiddevelopment2.registration.R as RegisterR
import com.example.androiddevelopment2.recipe_details.R as RecipeDetailsR
import com.example.androiddevelopment2.graph.R as GraphR

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Nav.Provider {

    @Inject
    lateinit var nav: Nav
    private val mainContainerId = R.id.main_fragment_container
    private var navController: NavController? = null
    private val viewModel: AuthorizationViewModel by viewModels()

    @Inject lateinit var fcmRepository: FcmRepository
    @Inject lateinit var isUserAuthorizedUseCase: IsUserAuthorizedUseCase

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            Log.d("Permissions", "Разрешение на уведомления получено")
        } else {
            Log.w("Permissions", "Разрешение на уведомления отклонено")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        setupNavigation()
        viewModel.navigateBasedOnAuthState()
    }

    private fun setupNavigation() {
        if (navController == null) {
            val navHost = supportFragmentManager.findFragmentById(mainContainerId) as NavHostFragment
            navController = navHost.navController
        }
        nav.setNavProvider(navProvider = this)
    }

    override fun getNavController(): NavController? {
        return navController
    }

    override fun onResume() {
        super.onResume()
        handleNavigationPayload()
    }

    fun handleNavigationPayload() {
        if (!fcmRepository.shouldHandleNavigation()) return
            fcmRepository.getCategory3Payload()?.let { (screen, extras) ->
            lifecycleScope.launch {
                screen?.let {
                    handleNavigation(it, extras)
                    fcmRepository.clearCategory3Payload()
                }
            }
        }
    }

    private suspend fun handleNavigation(screen: ScreenDestination, extras: Map<String, String>?) {
        val isAuthorized = isUserAuthorizedUseCase()
        val currentDestinationId = navController?.currentDestination?.id

        when {
            !isAuthorized && screen !is ScreenDestination.Auth && screen !is ScreenDestination.Register -> {
                showToast("Для доступа к этой функции требуется авторизация")
            }
            currentDestinationId != null && isAlreadyOnScreen(screen, currentDestinationId) -> {
                showToast("Вы уже находитесь на этом экране")
            }
            !isAuthorized && (
                    (screen is ScreenDestination.Auth && currentDestinationId != AuthR.id.destination_authorization_fragment) ||
                            (screen is ScreenDestination.Register && currentDestinationId != RegisterR.id.destination_registration_fragment)
                    ) -> {
                navigateToScreen(screen, extras)
            }
            else -> {
                navigateToScreen(screen, extras)
            }
        }
    }

    private fun navigateToScreen(screen: ScreenDestination, extras: Map<String, String>?) {
        try {
            when (screen) {
                is ScreenDestination.RecipeDetails -> {
                    val recipeId = extras?.get("recipeId")?.toIntOrNull() ?: -1
                    if (recipeId != -1) {
                        nav.goToScreen(ScreenDestination.RecipeDetails(recipeId).route)
                    }
                }
                else -> {
                    nav.goToScreen(screen.route)
                }
            }
        } catch (e: Exception) {
        }
    }

    private fun isAlreadyOnScreen(screen: ScreenDestination, currentDestinationId: Int): Boolean {
        return when (screen) {
            ScreenDestination.Search -> currentDestinationId == SearchR.id.destination_search_fragment
            ScreenDestination.Graph -> currentDestinationId == GraphR.id.destination_graph_fragment
            ScreenDestination.Auth -> currentDestinationId == AuthR.id.destination_authorization_fragment
            ScreenDestination.Register -> currentDestinationId == RegisterR.id.destination_registration_fragment
            is ScreenDestination.RecipeDetails -> currentDestinationId == RecipeDetailsR.id.destination_recipe_details_fragment
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::nav.isInitialized) {
            nav.clearNavProvider(navProvider = this)
        }
    }
}
