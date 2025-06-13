package com.example.androiddevelopment2.graph

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.androiddevelopment2.graph.components.GraphCanvas
import com.example.androiddevelopment2.graph.components.GraphInputs
import androidx.compose.runtime.getValue
import com.example.androiddevelopment2.base.theme.Theme

@Composable
fun GraphScreen(
    viewModel: GraphViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                top = Theme.spacing.extraLarge + Theme.spacing.small,
                start = Theme.spacing.medium,
                end = Theme.spacing.medium,
                bottom = Theme.spacing.medium
            ),
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.medium)
    ) {
        GraphInputs(
            state = state,
            onEvent = viewModel::onEvent,
            modifier = Modifier.fillMaxWidth()
        )

        if (state.showGraph) {
            GraphCanvas(
                uiState = state.uiState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Theme.spacing.graphHeight)
                    .padding(start = Theme.spacing.medium, top = Theme.spacing.medium)
            )
        }
    }
}