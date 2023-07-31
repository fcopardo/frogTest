package com.pardo.frogmitest.domain.models.ui

/**
 * Classes to store viewModel data. Since state should be a single value, we want to wrap
 * groups of values meant to trigger rendering into a single group.
 */
sealed class ViewModelResult<T> {
    data class Success<T>(val value: T) : ViewModelResult<T>()
    data class Error<T>(val message : String) : ViewModelResult<T>()
}

data class ViewModelValue<T>(var data:T, var success : Boolean = true, var message : String = "")