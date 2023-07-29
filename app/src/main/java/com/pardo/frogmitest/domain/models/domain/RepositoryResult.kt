package com.pardo.frogmitest.domain.models.domain

class RepositoryResult<T> {
    constructor(success : Boolean = false, aMessage : String = "", myValue : T? = null){
        value = myValue
        message = aMessage
        successful = success
    }

    private var value : T? = null
    private var message : String = ""
    private var successful = false

    fun getValue() : T?{
        return value
    }

    fun getMessage() : String {
        return message
    }

    fun isSuccessful() : Boolean{
        return successful
    }
}