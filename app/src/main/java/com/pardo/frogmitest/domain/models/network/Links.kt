package com.pardo.frogmitest.domain.models.network

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("prev", "next", "first", "last", "self")
class Links {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("prev")
    @set:JsonProperty("prev")
    @JsonProperty("prev")
    var prev: String? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("next")
    @set:JsonProperty("next")
    @JsonProperty("next")
    var next: String? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("first")
    @set:JsonProperty("first")
    @JsonProperty("first")
    var first: String? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("last")
    @set:JsonProperty("last")
    @JsonProperty("last")
    var last: String? = null

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("self")
    @set:JsonProperty("self")
    @JsonProperty("self")
    var self: String? = null

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
