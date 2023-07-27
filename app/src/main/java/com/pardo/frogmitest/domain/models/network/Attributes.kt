package com.pardo.frogmitest.domain.models.network

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("name", "code", "active", "full_address", "coordinates", "created_at")
class Attributes {
    @get:JsonProperty("name")
    @set:JsonProperty("name")
    @JsonProperty("name")
    var name: String = ""

    @get:JsonProperty("code")
    @set:JsonProperty("code")
    @JsonProperty("code")
    var code: String = ""

    @get:JsonProperty("active")
    @set:JsonProperty("active")
    @JsonProperty("active")
    var active: Boolean = false

    @get:JsonProperty("full_address")
    @set:JsonProperty("full_address")
    @JsonProperty("full_address")
    var fullAddress: String = ""

    @get:JsonProperty("coordinates")
    @set:JsonProperty("coordinates")
    @JsonProperty("coordinates")
    var coordinates: Coordinates? = null

    @get:JsonProperty("created_at")
    @set:JsonProperty("created_at")
    @JsonProperty("created_at")
    var createdAt: String = ""

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
