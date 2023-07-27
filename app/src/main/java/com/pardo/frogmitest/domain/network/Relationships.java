
package com.pardo.frogmitest.domain.network;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "brands",
    "zones"
})

public class Relationships {

    @JsonProperty("brands")
    private Brands brands;
    @JsonProperty("zones")
    private Zones zones;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("brands")
    public Brands getBrands() {
        return brands;
    }

    @JsonProperty("brands")
    public void setBrands(Brands brands) {
        this.brands = brands;
    }

    @JsonProperty("zones")
    public Zones getZones() {
        return zones;
    }

    @JsonProperty("zones")
    public void setZones(Zones zones) {
        this.zones = zones;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
