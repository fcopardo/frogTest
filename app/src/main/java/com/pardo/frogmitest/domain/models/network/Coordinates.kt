package com.pardo.frogmitest.domain.models.network

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("latitude", "longitude")
class Coordinates {
    @get:JsonProperty("latitude")
    @set:JsonProperty("latitude")
    @JsonProperty("latitude")
    var latitude: Double? = null

    @get:JsonProperty("longitude")
    @set:JsonProperty("longitude")
    @JsonProperty("longitude")
    var longitude: Double? = null

    @JsonIgnore
    private val additionalProperties: MutableMap<String, Any> = LinkedHashMap()
    @JsonAnyGetter
    fun getAdditionalProperties(): Map<String, Any> {
        return additionalProperties
    }

    @JsonAnySetter
    fun setAdditionalProperty(name: String, value: Any) {
        additionalProperties[name] = value
    }
}
