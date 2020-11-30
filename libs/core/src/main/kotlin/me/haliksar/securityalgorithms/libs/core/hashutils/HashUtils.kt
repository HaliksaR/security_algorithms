package me.haliksar.securityalgorithms.libs.core.hashutils

import com.google.common.hash.Hashing
import java.nio.charset.Charset
import java.security.MessageDigest


val ByteArray.SHA_512: ByteArray
    get() = MessageDigest.getInstance("SHA-512").digest(this)

val ByteArray.SHA_256: ByteArray
    get() = MessageDigest.getInstance("SHA-256").digest(this)

val ByteArray.SHA_1: ByteArray
    get() = MessageDigest.getInstance("SHA-1").digest(this)

val Byte.SHA_256L: Long
    get() {
        val bytes = ByteArray(1)
        bytes[0] = this
        val bytesHash = MessageDigest.getInstance("SHA-256").digest(bytes)
        return bytesHash[0] + 128L
    }

val Byte.SHA_256B: Byte
    get() {
        val bytes = ByteArray(1)
        bytes[0] = this
        return MessageDigest.getInstance("SHA-256").digest(bytes)[0]
    }

val String.md5L: Long
    get() = Hashing.md5()
        .hashString(this, Charset.defaultCharset())
        .asLong()