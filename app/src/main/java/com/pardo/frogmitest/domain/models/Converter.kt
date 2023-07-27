package com.pardo.frogmitest.domain.models

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.pardo.frogmitest.domain.models.network.Attributes
import com.pardo.frogmitest.domain.models.network.Datum
import com.pardo.frogmitest.domain.models.ui.StoreCellData
import kotlin.reflect.KClass

class Converter {
    companion object {

        private var mapper : ObjectMapper = ObjectMapper().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false).configure(
            DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false)
        fun jsonToStoreCellData(attributes : Attributes) : StoreCellData {
            return StoreCellData(attributes.name, attributes.code, attributes.fullAddress)
        }

        fun jsonToStoreCellData(datum: Datum) : StoreCellData? {
            datum.attributes?.let {
                return jsonToStoreCellData(it)
            }
            return null
        }

        fun <T: Any> deserialize(json: String, clazz: KClass<T>): T? {
            if(json.isNullOrEmpty()) return null
            return mapper.readValue(json, clazz.java)
        }
    }
}