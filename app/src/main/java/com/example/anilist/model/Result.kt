package com.example.anilist.model

sealed class Result<T> {
    class Success<T>(val data: T) : Result<T>()
    class Error<T>(val errorType: ErrorType, val message: String? = null) : Result<T>()
}