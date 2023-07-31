package com.pardo.frogmitest.domain.models.domain

class PaginatedData<T> {
    private var data : T? = null
    private var currentPage = 0
    private var lastPage = 0

    fun setData(data : T) : PaginatedData<T> {
        this.data = data
        return this
    }
    fun setCurrentPage(page : Int) : PaginatedData<T> {
        this.currentPage = page
        return this
    }
    fun setLastPage(lastPage : Int) : PaginatedData<T> {
        this.lastPage = lastPage
        return this
    }

    fun getData() : T? {
        return data
    }

    fun getCurrentPage() : Int {
        return currentPage
    }

    fun getLastPage() : Int {
        return lastPage
    }
}