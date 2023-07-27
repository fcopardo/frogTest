package com.pardo.frogmitest.testModels

import com.fasterxml.jackson.annotation.JsonProperty


class Meme {
    var id: String = ""
    var name: String = ""
    var url: String = ""
    var width: Int = 0
    var height: Int = 0
    @JsonProperty("box_count")
    var boxCount: Int = 0
    var captions : Int =0
}

class Memes{
    var memes: List<Meme> = mutableListOf()
}

class MemeResponse{
    var success: Boolean = false
    var data: Memes? = null
}