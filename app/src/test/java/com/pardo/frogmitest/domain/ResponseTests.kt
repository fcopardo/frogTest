package com.pardo.frogmitest.domain

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.pardo.frogmitest.LoggerJVM
import com.pardo.frogmitest.domain.models.Converter
import com.pardo.frogmitest.domain.models.network.StoresResponse
import com.pardo.frogmitest.domain.models.ui.StoreCellData
import com.pardo.frogmitest.platformUtils.LoggerProvider
import org.junit.Assert.fail
import org.junit.Test

class ResponseTests {

    private var mapper : ObjectMapper = ObjectMapper().configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false).configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false)

    init {
        LoggerProvider.setLogger(LoggerJVM())
    }

    var json = "{\n" +
            "  \"data\": [\n" +
            "    {\n" +
            "      \"id\": \"984b32db-dadd-4dce-90d3-c5f1786ad18e\",\n" +
            "      \"type\": \"stores\",\n" +
            "      \"attributes\": {\n" +
            "        \"name\": \"Store3\",\n" +
            "        \"code\": \"STCT0000003\",\n" +
            "        \"active\": true,\n" +
            "        \"full_address\": \"Presidente Errazuriz 4125 , Santiago, Chile\",\n" +
            "        \"coordinates\": {\n" +
            "          \"latitude\": -33.41991,\n" +
            "          \"longitude\": -70.585991\n" +
            "        },\n" +
            "        \"created_at\": \"2019-03-11T20:06:21Z\"\n" +
            "      },\n" +
            "      \"links\": {\n" +
            "        \"self\": \"https://neo.frogmi.com/api/v3/stores/984b32db-dadd-4dce-90d3-c5f1786ad18e\"\n" +
            "      },\n" +
            "      \"relationships\": {\n" +
            "        \"brands\": {\n" +
            "          \"data\": {\n" +
            "            \"id\": \"54b2cdc3-623d-47b5-b00c-0448afedc8b1\",\n" +
            "            \"type\": \"brands\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"zones\": {\n" +
            "          \"data\": {\n" +
            "            \"id\": \"a2a7f7d8-62fa-43a5-9451-f98edc7b6454\",\n" +
            "            \"type\": \"zones\"\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  ],\n" +
            "  \"meta\": {\n" +
            "    \"pagination\": {\n" +
            "      \"current_page\": 1,\n" +
            "      \"total\": 121,\n" +
            "      \"per_page\": 10\n" +
            "    }\n" +
            "  },\n" +
            "  \"links\": {\n" +
            "    \"prev\": null,\n" +
            "    \"next\": \"https://neo.frogmi.com/api/v3/stores?per_page=10&page=2\",\n" +
            "    \"first\": \"https://neo.frogmi.com/api/v3/stores?per_page=10&page=1\",\n" +
            "    \"last\": \"https://neo.frogmi.com/api/v3/stores?per_page=10&page=121\",\n" +
            "    \"self\": \"https://neo.frogmi.com/api/v3/stores?per_page=10&page=1\"\n" +
            "  }\n" +
            "}"

    @Test
    fun storeResponseTest(){

        var parsed : StoresResponse? = mapper.readValue(json, StoresResponse::class.java)

        assert(parsed != null)
        parsed?.let {
            it.data?.let { datumList ->

                assert(datumList.size == 1)
                assert(datumList[0] != null)
                assert(datumList[0].attributes != null)
                assert(datumList[0].attributes!!.name == "Store3")
                assert(datumList[0].attributes!!.coordinates!!.latitude == -33.41991)
                assert(datumList[0].relationships !=null)
                assert(datumList[0].relationships!!.brands!!.data!!.id == "54b2cdc3-623d-47b5-b00c-0448afedc8b1")

                LoggerProvider.getLogger()?.log("", "")
                LoggerProvider.getLogger()?.log("JsonTests", "all ok!")
                LoggerProvider.getLogger()?.log("", "")

            } ?: run {
                fail("Top level data was empty")
            }
        } ?: run {
            fail("JSON could not be parsed")
        }
    }

    @Test
    fun jsonToUIData(){
        val parsed : StoresResponse? = mapper.readValue(json, StoresResponse::class.java)
        parsed?.let {
            it.data!![0].attributes?.let {attributes ->
                val storeCellData = Converter.jsonToStoreCellData(attributes)
                println("")
                LoggerProvider.getLogger()?.log("Json to StoreCellData test", "Cell[name:${storeCellData.name} code:${storeCellData.code} address:${storeCellData.fullAddress}]")
                println("")
                assert(storeCellData.name == "Store3")
                assert(storeCellData.code == "STCT0000003")
                assert(storeCellData.fullAddress == "Presidente Errazuriz 4125 , Santiago, Chile")
            } ?: run {
                fail("attributes was empty")
            }
        } ?: run {
            fail("JSON could not be parsed")
        }
    }




}