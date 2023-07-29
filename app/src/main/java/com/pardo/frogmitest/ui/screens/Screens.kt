package com.pardo.frogmitest.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pardo.frogmitest.domain.models.ui.StoreCellData
import com.pardo.frogmitest.domain.models.ui.ViewModelResult
import com.pardo.frogmitest.ui.viewmodels.StoresViewModel
import com.pardo.frogmitest.ui.compositions.MainScreenWidgets

@Composable
fun StoresScreen(model: StoresViewModel = viewModel()){
    val storeState by model.stores.collectAsState()
    Column(modifier = Modifier
        .fillMaxWidth()) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "MyApp",
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center).padding(6.dp)
            )
        }
        storeState?.let {
            if(it is ViewModelResult.Success){
                var safeList = if(it.value!=null){
                    it.value as List<StoreCellData>
                } else {
                    null
                }
                safeList?.let{ safe->
                    MainScreenWidgets.StoreList(stores = safe)
                }
            } else {
                it as ViewModelResult.Error
                Text(
                    text = it.message,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
