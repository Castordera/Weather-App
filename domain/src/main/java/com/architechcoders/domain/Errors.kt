package com.architechcoders.domain

sealed interface Errors {
    sealed interface Device: Errors {
        object NoNetwork: Device
    }
    sealed interface Server: Errors {
        object Unauthorized: Server
    }
    sealed interface Database: Errors {
        object SaveFailed: Database
    }
    class Unknown(val message: String): Errors
}