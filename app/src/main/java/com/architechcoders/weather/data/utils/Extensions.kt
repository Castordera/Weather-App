package com.architechcoders.weather.data.utils

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import android.util.Log
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.architechcoders.domain.Errors
import retrofit2.HttpException
import java.net.HttpURLConnection
import java.net.UnknownHostException

const val TAG = "Error"

suspend fun <T> call(action: suspend () -> T): Either<Errors, T> = try {
    action().right()
} catch (error: Exception) {
    Log.e(TAG, "Error", error)
    error.toError().left()
}

fun Throwable.toError(): Errors = when (this) {
    is UnknownHostException -> Errors.Device.NoNetwork
    is HttpException -> {
        when (code()) {
            HttpURLConnection.HTTP_UNAUTHORIZED -> Errors.Server.Unauthorized
            else -> Errors.Unknown(localizedMessage ?: "")
        }
    }
    is SQLiteException -> {
        when (this) {
            is SQLiteConstraintException -> Errors.Database.SaveFailed
            else -> Errors.Unknown(localizedMessage ?: "")
        }
    }
    else -> Errors.Unknown(localizedMessage ?: "")
}