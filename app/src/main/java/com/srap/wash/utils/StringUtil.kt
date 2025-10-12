package com.srap.wash.utils

import java.security.MessageDigest

object StringUtil {
    fun sha256(input: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(input.toByteArray())
        return hash.joinToString("") { String.format("%02x", it) }
    }
}