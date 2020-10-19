package me.haliksar.securityalgorithms.libs.ciphers

import me.haliksar.securityalgorithms.libs.ciphers.encrypt.ElGamaliaCipherLong
import me.haliksar.securityalgorithms.libs.ciphers.encrypt.RsaCipherLong
import me.haliksar.securityalgorithms.libs.ciphers.encrypt.ShamirCipherLong
import me.haliksar.securityalgorithms.libs.ciphers.encrypt.VernamCipherLong
import me.haliksar.securityalgorithms.libs.ciphers.signature.ElGamaliaCipherSignatureLong
import me.haliksar.securityalgorithms.libs.ciphers.signature.GostElectronicSignatureLong
import me.haliksar.securityalgorithms.libs.ciphers.signature.RsaCipherSignatureLong
import me.haliksar.securityalgorithms.libs.ciphers.wrapper.EncryptWrapper
import me.haliksar.securityalgorithms.libs.ciphers.wrapper.SignatureWrapper
import me.haliksar.securityalgorithms.libs.core.fileutils.fileToByteArray
import me.haliksar.securityalgorithms.libs.core.fileutils.fileToLongList
import me.haliksar.securityalgorithms.libs.core.fileutils.writeTo


fun shamirCipherLong(dataSource: Pair<String, String>, dump: Boolean = true) {
    val path = "$resource/ShamirCipher"
    val file = "$resource/${dataSource.first}${dataSource.second}".fileToLongList(dump)
    val method = ShamirCipherLong()
    val wrapper = EncryptWrapper("ShamirCipher", method, dump)
    wrapper.generate()
    method.keys?.writeTo("$path/keys/", "${dataSource.first}_keys.txt", dump)
    val encrypt = wrapper.encrypt(file, true)
    encrypt.writeTo("$path/encrypt/", "${dataSource.first}_encrypt${dataSource.second}", dump)
    val decrypt = wrapper.decrypt(encrypt, true)
    decrypt.writeTo("$path/decrypt/", "${dataSource.first}_decrypt${dataSource.second}", dump)
}

fun vernamCipherLong(dataSource: Pair<String, String>, dump: Boolean = true) {
    val path = "$resource/VernamCipher"
    val file = "$resource/${dataSource.first}${dataSource.second}".fileToLongList(dump)
    val method = VernamCipherLong()
    val wrapper = EncryptWrapper("VernamCipher", method, dump)
    method.keys?.writeTo("$path/keys/", "${dataSource.first}_keys.txt")
    val encrypt = wrapper.encrypt(file, false)
    encrypt.writeTo("$path/encrypt/", "${dataSource.first}_encrypt${dataSource.second}", dump)
    val decrypt = wrapper.decrypt(encrypt, true)
    decrypt.writeTo("$path/decrypt/", "${dataSource.first}_decrypt${dataSource.second}", dump)
}

fun rsaCipherLong(dataSource: Pair<String, String>, dump: Boolean = true) {
    val path = "$resource/RsaCipher"
    val file = "$resource/${dataSource.first}${dataSource.second}".fileToLongList(dump)
    val method = RsaCipherLong()
    val wrapper = EncryptWrapper("RsaCipher", method, dump)
    wrapper.generate()
    method.keys?.writeTo("$path/keys/", "${dataSource.first}_keys.txt", dump)
    val encrypt = wrapper.encrypt(file, false)
    encrypt.writeTo("$path/encrypt/", "${dataSource.first}_encrypt${dataSource.second}", dump)
    val decrypt = wrapper.decrypt(encrypt, true)
    decrypt.writeTo("$path/decrypt/", "${dataSource.first}_decrypt${dataSource.second}", dump)
}

fun rsaCipherLongSignature(dataSource: Pair<String, String>, dump: Boolean = true) {
    val path = "$resource/RsaCipherSignature"
    val file = "$resource/${dataSource.first}${dataSource.second}".fileToByteArray(dump)
    val method = RsaCipherSignatureLong()
    val wrapper = SignatureWrapper("RsaCipherSignature", method, dump)
    wrapper.generate()
    method.keys?.writeTo("$path/keys/", "${dataSource.first}_keys.txt", dump)
    val hash = wrapper.sing(file.toList(), false)
    val verify = wrapper.verify(hash, false)
    verify.writeTo("$path/verify/", "${dataSource.first}_verify.txt", dump)
}

fun elGamaliaCipherLong(dataSource: Pair<String, String>, dump: Boolean = true) {
    val path = "$resource/ElGamaliaCipher"
    val file = "$resource/${dataSource.first}${dataSource.second}".fileToLongList(dump)
    val method = ElGamaliaCipherLong()
    val wrapper = EncryptWrapper("ElGamaliaCipher", method, dump)
    wrapper.generate()
    method.keys?.writeTo("$path/keys/", "${dataSource.first}_keys.txt", dump)
    val encrypt = wrapper.encrypt(file, false)
    encrypt.writeTo("$path/encrypt/", "${dataSource.first}_encrypt${dataSource.second}", dump)
    val decrypt = wrapper.decrypt(encrypt, true)
    decrypt.writeTo("$path/decrypt/", "${dataSource.first}_decrypt${dataSource.second}", dump)
}

fun elGamaliaCipherLongSignature(dataSource: Pair<String, String>, dump: Boolean = true) {
    val path = "$resource/ElGamaliaCipherSignature"
    val file = "$resource/${dataSource.first}${dataSource.second}".fileToByteArray(dump)
    val method = ElGamaliaCipherSignatureLong()
    val wrapper = SignatureWrapper("ElGamaliaCipherSignature", method, dump)
    wrapper.generate()
    method.keys?.writeTo("$path/keys/", "${dataSource.first}_keys.txt", dump)
    val hash = wrapper.sing(file.toList(), false)
    val verify = wrapper.verify(hash, false)
    verify.writeTo("$path/verify/", "${dataSource.first}_verify.txt", dump)
}

fun gostElectronicSignatureLongSignature(dataSource: Pair<String, String>, dump: Boolean = true) {
    val path = "$resource/GostElectronicSignature"
    val file = "$resource/${dataSource.first}${dataSource.second}".fileToByteArray(dump)
    val method = GostElectronicSignatureLong()
    val wrapper = SignatureWrapper("GostElectronicSignature", method, dump)
    wrapper.generate()
    method.keys?.writeTo("$path/keys/", "${dataSource.first}_keys.txt", dump)
    val hash = wrapper.sing(file.toList(), true)
    val verify = wrapper.verify(hash, true)
    verify.writeTo("$path/verify/", "${dataSource.first}_verify.txt", dump)
}