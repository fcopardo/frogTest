package com.pardo.frogmitest.domain.models

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.pardo.frogmitest.domain.models.network.Attributes
import com.pardo.frogmitest.domain.models.network.Datum
import com.pardo.frogmitest.domain.models.network.Links
import com.pardo.frogmitest.domain.models.ui.StoreCellData
import kotlin.reflect.KClass

class Converter {
    companion object {

        private var mapper : ObjectMapper = ObjectMapper().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false).configure(
            DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false)
        fun jsonToStoreCellData(attributes : Attributes, id : String? = "") : StoreCellData {
            attributes.coordinates?.let {
                return StoreCellData(attributes.name, attributes.code, attributes.fullAddress, id!!, it.latitude, it.longitude)
            } ?: run {
                return StoreCellData(attributes.name, attributes.code, attributes.fullAddress, id!!)
            }
        }

        fun jsonToStoreCellData(datum: Datum) : StoreCellData? {
            datum.attributes?.let {
                return jsonToStoreCellData(it, datum.id)
            }
            return null
        }

        fun pageFromLinks(url : String): Int? {
            val regex = """&page=(\d+)""".toRegex()
            val matchResult = regex.find(url)
            return matchResult?.groups?.get(1)?.value?.toInt()
        }

        fun <T: Any> deserialize(json: String, clazz: KClass<T>): T? {
            if(json.isNullOrEmpty()) return null
            return mapper.readValue(json, clazz.java)
        }
    }
}