package com.pardo.frogmitest.domain

import com.pardo.frogmitest.domain.models.Converter
import org.junit.Test

class ConverterTests {
    @Test
    fun getPageTest(){
        var url = "https://neo.frogmi.com/api/v3/stores?per_page=10&page=121"
        println("converter: ${Converter.pageFromLinks(url)}")
        assert(121 == Converter.pageFromLinks(url))

        var url2 = "https://neo.frogmi.com/api/v3/stores?per_page=10&page=1"
        println("converter: ${Converter.pageFromLinks(url2)}")
        assert(1 == Converter.pageFromLinks(url2))

    }
}