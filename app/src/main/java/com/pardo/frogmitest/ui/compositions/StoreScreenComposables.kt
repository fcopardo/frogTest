package com.pardo.frogmitest.ui.compositions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pardo.frogmitest.domain.models.ui.StoreCellData

/**
 * Exposing composables this way allows to have stateless previews without risking somebody
 * to use them somewhere.
 */
class StoreScreenComposables {
    companion object{
        @Composable
        fun StoreCell(storeCellData: StoreCellData, onClick: (item: StoreCellData) -> kotlin.Unit = {}, modifier : Modifier = Modifier) {
            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                        modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = modifier.padding(16.dp).clickable {
                    onClick(storeCellData)
                }) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = storeCellData.name, modifier = Modifier.shadow(elevation = 4.dp))
                        Text(text = "|")
                        Text(text = storeCellData.code, modifier = Modifier.shadow(elevation = 4.dp))
                    }
                    Text(text = storeCellData.fullAddress, style = TextStyle(fontStyle = FontStyle.Italic))
                }
            }
        }

        @Composable
        fun StoreList(stores: List<StoreCellData>, myState: LazyListState = rememberLazyListState(), onClick : (item : StoreCellData)->Unit = {}, modifier: Modifier = Modifier){
            LazyColumn(
                state = myState,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(stores, key = {it.id}){ store ->
                    StoreCell(storeCellData = store, onClick)
                }
            }
        }
    }

    @Composable
    @Preview
    private fun StoreCellDemo(){
        StoreCell(StoreCellData("Store 1", "STCT000000", "Presidente Errazuriz 1421, Santiago, Chile", ""))
    }

    @Composable
    @Preview
    private fun StoreListDemo(){
        var cellA = StoreCellData("Store 1", "STCT000000", "Presidente Errazuriz 1421, Santiago, Chile", "a")
        var cellB = StoreCellData("Store 2", "STCT000001", "Moneda 1022, Santiago, Chile", "b")
        var cellC = StoreCellData("Store 3", "STCT000003", "Moneda 1022, Santiago, Chile", "c")
        var cellD = StoreCellData("Store 4", "STCT000004", "Escandinavia 152, Santiago, Chile", "d")
        var myList = mutableListOf<StoreCellData>().apply { 
            add(cellA)
            add(cellB)
            add(cellC)
            add(cellD)
        }
        StoreList(stores = myList)
    }
}