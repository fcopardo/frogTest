package com.pardo.frogmitest.domain.models.network

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("id", "type", "attributes", "links", "relationships")
class Datum {
    @get:JsonProperty("id")
    @set:JsonProperty("id")
    @JsonProperty("id")
    var id: String? = null

    @get:JsonProperty("type")
    @set:JsonProperty("type")
    @JsonProperty("type")
    var type: String? = null

    @get:JsonProperty("attributes")
    @set:JsonProperty("attributes")
    @JsonProperty("attributes")
    var attributes: Attributes? = null

    @get:JsonProperty("links")
    @set:JsonProperty("links")
    @JsonProperty("links")
    var links: Links? = null

    @get:JsonProperty("relationships")
    @set:JsonProperty("relationships")
    @JsonProperty("relationships")
    var relationships: Relationships? = null

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
