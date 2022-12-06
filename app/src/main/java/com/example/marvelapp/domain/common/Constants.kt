package com.example.marvelapp.domain.common

import java.math.BigInteger
import java.security.MessageDigest
import java.sql.Timestamp

object Constants {
    const val API_BASE_URL = "https://gateway.marvel.com"
    const val API_KEY = "fe7e5c8d2fec015305ace65babb72401"
    const val API_PRIVATE_KEY = "6512f307917ab54c846fcb24d0ffa671c5ce104c"
    val timeStamp = Timestamp(System.currentTimeMillis()).time.toString()

    fun toMd5Hash(): String {
        val str = "$timeStamp$API_PRIVATE_KEY$API_KEY"
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(str.toByteArray())).toString(16).padStart(32, '0')
    }

    //Query param API
    const val BY_NAME = "name"
    const val BY_MODIFIED = "modified"
    const val BY_NAME_DESCENDING = "-name"
    const val BY_MODIFIED_DESCENDING = "-modified"

    //Data Store
    const val USER_PREF_NAME = "user_preference"
}