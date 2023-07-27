package com.pardo.frogmitest.domain.models

import com.pardo.frogmitest.domain.models.network.Attributes
import com.pardo.frogmitest.domain.models.network.Datum
import com.pardo.frogmitest.domain.models.ui.StoreCellData

class Converter {
    companion object{
        fun jsonToStoreCellData(attributes : Attributes) : StoreCellData {
            return StoreCellData(attributes.name, attributes.code, attributes.fullAddress)
        }

        fun jsonToStoreCellData(datum: Datum) : StoreCellData? {
            datum.attributes?.let {
                return jsonToStoreCellData(it)
            }
            return null
        }
    }
}