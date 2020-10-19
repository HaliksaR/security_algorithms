package me.haliksar.securityalgorithms.libs.ciphers

import me.haliksar.securityalgorithms.libs.ciphers.encrypt.ElGamaliaCipherLong
import me.haliksar.securityalgorithms.libs.ciphers.encrypt.RsaCipherLong
import me.haliksar.securityalgorithms.libs.ciphers.encrypt.ShamirCipherLong
import me.haliksar.securityalgorithms.libs.ciphers.encrypt.VernamCipherLong
import me.haliksar.securityalgorithms.libs.ciphers.signature.ElGamaliaSignatureLong
import me.haliksar.securityalgorithms.libs.ciphers.signature.GostSignatureLong
import me.haliksar.securityalgorithms.libs.ciphers.signature.RsaCipherSignatureLong
import me.haliksar.securityalgorithms.libs.ciphers.wrapper.EncryptWrapper
import me.haliksar.securityalgorithms.libs.ciphers.wrapper.SignatureWrapper
import me.haliksar.securityalgorithms.libs.core.fileutils.fileToByteList
import me.haliksar.securityalgorithms.libs.core.fileutils.fileToLongList


fun shamirCipherLong(dataSource: Pair<String, String>, dump: Boolean = true): String =
    EncryptWrapper("ShamirCipher", ShamirCipherLong(), dump)
        .start(
            path = "$resource/ShamirCipher",
            data = "$resource/${dataSource.first}${dataSource.second}".fileToLongList(dump),
            dataSource = dataSource,
            encryptParallel = true,
            decryptParallel = true
        )

fun vernamCipherLong(dataSource: Pair<String, String>, dump: Boolean = true): String =
    EncryptWrapper("VernamCipher", VernamCipherLong(), dump)
        .start(
            path = "$resource/VernamCipher",
            data = "$resource/${dataSource.first}${dataSource.second}".fileToLongList(dump),
            dataSource = dataSource,
            encryptParallel = false,
            decryptParallel = true
        )

fun rsaCipherLong(dataSource: Pair<String, String>, dump: Boolean = true): String =
    EncryptWrapper("RsaCipher", RsaCipherLong(), dump)
        .start(
            path = "$resource/RsaCipher",
            data = "$resource/${dataSource.first}${dataSource.second}".fileToLongList(dump),
            dataSource = dataSource,
            encryptParallel = true,
            decryptParallel = true
        )

fun elGamaliaCipherLong(dataSource: Pair<String, String>, dump: Boolean = true): String =
    EncryptWrapper("ElGamaliaCipher", ElGamaliaCipherLong(), dump)
        .start(
            path = "$resource/ElGamaliaCipher",
            data = "$resource/${dataSource.first}${dataSource.second}".fileToLongList(dump),
            dataSource = dataSource,
            encryptParallel = true,
            decryptParallel = true
        )

fun rsaLongSignature(dataSource: Pair<String, String>, dump: Boolean = true): String =
    SignatureWrapper("RsaSignature", RsaCipherSignatureLong(), dump).start(
        path = "$resource/RsaSignature",
        data = "$resource/${dataSource.first}${dataSource.second}".fileToByteList(dump),
        dataSource = dataSource,
        verifyParallel = true,
        singParallel = true
    )

fun elGamaliaLongSignature(dataSource: Pair<String, String>, dump: Boolean = true): String =
    SignatureWrapper("ElGamaliaSignature", ElGamaliaSignatureLong(), dump).start(
        path = "$resource/ElGamaliaSignature",
        data = "$resource/${dataSource.first}${dataSource.second}".fileToByteList(dump),
        dataSource = dataSource,
        verifyParallel = true,
        singParallel = false
    )

fun gostLongSignature(dataSource: Pair<String, String>, dump: Boolean = true): String =
    SignatureWrapper("GostSignature", GostSignatureLong(), dump).start(
        path = "$resource/GostSignature",
        data = "$resource/${dataSource.first}${dataSource.second}".fileToByteList(dump),
        dataSource = dataSource,
        verifyParallel = true,
        singParallel = true
    )