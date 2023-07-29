package com.pardo.frogmitest.domain.models.ui

sealed class ViewModelResult<out T> {
    data class Success<out T>(val value: Any?) : ViewModelResult<T>()
    data class Error(val message : String) : ViewModelResult<Nothing>()
}