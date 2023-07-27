package com.pardo.frogmitest.domain.models.network

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("data", "meta", "links")
class StoresResponse {
    @get:JsonProperty("data")
    @set:JsonProperty("data")
    @JsonProperty("data")
    var data: List<Datum>? = null

    @get:JsonProperty("meta")
    @set:JsonProperty("meta")
    @JsonProperty("meta")
    var meta: Meta? = null

    @get:JsonProperty("links")
    @set:JsonProperty("links")
    @JsonProperty("links")
    var links: Links? = null

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
