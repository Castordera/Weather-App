package com.architechcoders.domain

sealed interface Errors {
    class Server(val code: Int): Errors
    class Database(val message: String): Errors
    class Unknown(val message: String): Errors
}