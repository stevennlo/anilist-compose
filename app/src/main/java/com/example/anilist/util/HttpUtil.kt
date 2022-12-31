package com.example.anilist.util

import android.content.Context
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloNetworkException
import com.example.anilist.R
import com.example.anilist.model.ErrorType
import com.example.anilist.model.Result
import java.io.IOException

object HttpUtil {

    fun <T> Throwable.getErrorResult(): Result<T> {
        printStackTrace()
        return when (this) {
            is ApolloNetworkException -> {
                if (platformCause is IOException) Result.Error(ErrorType.NETWORK_ERROR)
                else Result.Error(ErrorType.GENERAL_ERROR)
            }
            else -> Result.Error(ErrorType.GENERAL_ERROR)
        }
    }

    fun <T, D : Operation.Data> ApolloResponse<D>.toResult(parser: (D) -> T): Result<T> {
        return if (errors.isNullOrEmpty()) {
            Result.Success(parser.invoke(dataAssertNoErrors))
        } else {
            Result.Error(ErrorType.GENERAL_ERROR, errors?.first()?.message)
        }
    }

    fun <T> Result.Error<T>.getErrorMessage(context: Context): String {
        return message ?: getErrorMessage(context, errorType)
    }

    private fun getErrorMessage(context: Context, errorType: ErrorType): String {
        val resourceId = when (errorType) {
            ErrorType.NETWORK_ERROR -> R.string.network_error_message
            ErrorType.GENERAL_ERROR -> R.string.general_error_message
        }

        return context.getString(resourceId)
    }

    fun <T> T?.orAbsent(): Optional<T?> {
        return this?.let { Optional.Present(it) } ?: Optional.Absent
    }
}