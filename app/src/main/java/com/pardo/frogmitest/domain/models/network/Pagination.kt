package com.pardo.frogmitest.domain.models.network

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("current_page", "total", "per_page")
class Pagination {
    @get:JsonProperty("current_page")
    @set:JsonProperty("current_page")
    @JsonProperty("current_page")
    var currentPage: Int? = null

    @get:JsonProperty("total")
    @set:JsonProperty("total")
    @JsonProperty("total")
    var total: Int? = null

    @get:JsonProperty("per_page")
    @set:JsonProperty("per_page")
    @JsonProperty("per_page")
    var perPage: Int? = null

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
