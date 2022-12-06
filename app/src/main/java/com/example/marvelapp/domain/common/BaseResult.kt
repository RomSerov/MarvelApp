package com.example.marvelapp.domain.common

sealed class BaseResult<T>(val data: T? = null, val message: String? = null) {

    class Loading<T>(data: T? = null) : BaseResult<T>(data = data)

    class Success<T>(data: T) : BaseResult<T>(data = data)

    class Error<T>(data: T? = null, message: String) : BaseResult<T>(data = data, message = message)
}