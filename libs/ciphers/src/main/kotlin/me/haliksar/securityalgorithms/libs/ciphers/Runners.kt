package me.haliksar.securityalgorithms.libs.ciphers

import me.haliksar.securityalgorithms.libs.ciphers.encrypt.ElGamaliaCipher
import me.haliksar.securityalgorithms.libs.ciphers.encrypt.RsaCipher
import me.haliksar.securityalgorithms.libs.ciphers.encrypt.ShamirCipher
import me.haliksar.securityalgorithms.libs.ciphers.encrypt.VernamCipher
import me.haliksar.securityalgorithms.libs.ciphers.signature.ElGamaliaSignature
import me.haliksar.securityalgorithms.libs.ciphers.signature.GostSignature
import me.haliksar.securityalgorithms.libs.ciphers.signature.RsaCipherSignature
import me.haliksar.securityalgorithms.libs.ciphers.wrapper.EncryptWrapper
import me.haliksar.securityalgorithms.libs.ciphers.wrapper.SignatureWrapper
import me.haliksar.securityalgorithms.libs.core.fileutils.fileToByteList
import me.haliksar.securityalgorithms.libs.core.fileutils.fileToLongList
import me.haliksar.securityalgorithms.libs.core.hashutils.SHA_256L


suspend fun Resource.shamirCipher(dump: Boolean = true): String =
    EncryptWrapper("ShamirCipher", ShamirCipher(), dump, this)
        .start(
            path = "$resources/ShamirCipher",
            data = "$resources/${file}".fileToLongList(dump),
            encryptParallel = true,
            decryptParallel = true
        )

suspend fun Resource.vernamCipher(dump: Boolean = true): String =
    EncryptWrapper("VernamCipher", VernamCipher(), dump, this)
        .start(
            path = "$resources/VernamCipher",
            data = "$resources/${file}".fileToLongList(dump),
            encryptParallel = true,
            decryptParallel = true
        )

suspend fun Resource.rsaCipher(dump: Boolean = true): String =
    EncryptWrapper("RsaCipher", RsaCipher(), dump, this)
        .start(
            path = "$resources/RsaCipher",
            data = "$resources/${file}".fileToLongList(dump),
            encryptParallel = true,
            decryptParallel = true
        )

suspend fun Resource.elGamaliaCipher(dump: Boolean = true): String =
    EncryptWrapper("ElGamaliaCipher", ElGamaliaCipher(), dump, this)
        .start(
            path = "$resources/ElGamaliaCipher",
            data = "$resources/${file}"
                .fileToLongList(dump),
            encryptParallel = true,
            decryptParallel = true
        )

suspend fun Resource.rsaSignature(dump: Boolean = true): String =
    SignatureWrapper("RsaSignature", RsaCipherSignature(), dump, this).start(
        path = "$resources/RsaSignature",
        data = "$resources/${file}"
            .fileToByteList(dump).map { it.SHA_256L },
        verifyParallel = true,
        singParallel = true
    )

suspend fun Resource.elGamaliaSignature(dump: Boolean = true): String =
    SignatureWrapper("ElGamaliaSignature", ElGamaliaSignature(), dump, this).start(
        path = "$resources/ElGamaliaSignature",
        data = "$resources/${file}"
            .fileToByteList(dump).map { it.SHA_256L },
        verifyParallel = true,
        singParallel = true
    )

suspend fun Resource.gostSignature(dump: Boolean = true): String =
    SignatureWrapper("GostSignature", GostSignature(), dump, this).start(
        path = "$resources/GostSignature",
        data = "$resources/${file}"
            .fileToByteList(dump).map { it.SHA_256L },
        verifyParallel = true,
        singParallel = true
    )