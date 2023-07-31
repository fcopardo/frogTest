package com.pardo.frogmitest.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pardo.frogmitest.platform.LoggerProvider
import com.pardo.frogmitest.ui.viewmodels.StoresViewModel
import com.pardo.frogmitest.ui.compositions.StoreScreenComposables

/**
 * A screen would be the equivalent of a custom view dedicated to a user case. It hoist state from
 * the composables to make them simpler. State in compose should be simple,
 * observed from a single place, and pass down "primitive" values to all the composable functions
 * invoked by the composition, to reduce the risk of "re-rendering" (unintended screen updates).
 */
@Composable
fun StoresScreen(model: StoresViewModel = viewModel()){

    val listState = rememberLazyListState()
    val isAtBottom = !listState.canScrollForward

    val stores = model.storeState

    Column(modifier = Modifier
        .fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "MyApp",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(6.dp)
            )
        }

        if(stores.success){
            Crossfade(targetState = stores.data, label = "storeList") { screen ->
                if(screen.isNotEmpty()){
                    StoreScreenComposables.StoreList(stores = stores.data, listState)
                }
            }
        } else {
            Text(
                text = stores.message,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
        }
        LaunchedEffect(key1 = isAtBottom) {
            if (isAtBottom) {
                model.getStoresPage()
            }
        }
    }
}
