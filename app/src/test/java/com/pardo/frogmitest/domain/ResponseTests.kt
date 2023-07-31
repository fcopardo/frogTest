package com.pardo.frogmitest.domain

import com.pardo.frogmitest.JVMRestPlatformDependencies
import com.pardo.frogmitest.LoggerJVM
import com.pardo.frogmitest.domain.data.remote.RestClient
import com.pardo.frogmitest.domain.models.Converter
import com.pardo.frogmitest.domain.models.network.StoresResponse
import com.pardo.frogmitest.platform.LoggerProvider
import com.pardo.frogmitest.testModels.MemeResponse
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import org.junit.Assert.fail
import org.junit.Test

class ResponseTests {

    init {
        LoggerProvider.setLogger(LoggerJVM())
        RestClient.setPlatformDependencies(JVMRestPlatformDependencies())
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

        val parsed : StoresResponse? = Converter.deserialize(json, StoresResponse::class)

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
        val parsed : StoresResponse? = Converter.deserialize(json, StoresResponse::class)
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

    /**
     * Initially I assumed the CURL was just an example, so I started testing stuff with other services and values. Silly me.
     */
    @Test
    fun requestTest() = runTest {
        RestClient.getData("https://api.imgflip.com/get_memes", mutableMapOf(), MemeResponse::class).take(1).collect {
            when(it){
                is RestClient.NetworkResult.Success ->{
                    var data = it.value as MemeResponse
                    LoggerProvider.getLogger()?.log("Meme response", data.data?.memes!!.size.toString())
                }
                is RestClient.NetworkResult.Error -> {
                    val code = it.code
                    val type = it.type
                    LoggerProvider.getLogger()?.log("Meme error", "code:$code type:$type")
                    fail("rest client failed")
                }
            }
        }
    }
}