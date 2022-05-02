package com.architechcoders.domain

sealed interface Errors {
    class Unknown(val message: String): Errors
}