package com.pardo.frogmitest.domain.data.secure

class CredentialsProvider private constructor(){
    companion object{
        private var instance = CredentialsProvider()
        fun getInstance(): CredentialsProvider {
            return instance
        }
    }
    private var token = ""
    private var companyId = ""

    fun setToken(token : String){
        this.token = token
    }

    fun getToken(): String {
        return token
    }

    fun setCompanyId(id : String){
        this.companyId = id
    }

    fun getCompanyId(): String {
        return companyId
    }
}