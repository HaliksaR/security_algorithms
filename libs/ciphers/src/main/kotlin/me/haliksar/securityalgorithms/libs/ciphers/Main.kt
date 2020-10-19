package me.haliksar.securityalgorithms.libs.ciphers

import me.haliksar.securityalgorithms.libs.ciphers.cipher.*
import me.haliksar.securityalgorithms.libs.ciphers.wrapper.EncryptWrapper
import me.haliksar.securityalgorithms.libs.ciphers.wrapper.SignatureWrapper
import me.haliksar.securityalgorithms.libs.core.fileutils.fileToByteArray
import me.haliksar.securityalgorithms.libs.core.fileutils.fileToLongList
import me.haliksar.securityalgorithms.libs.core.fileutils.writeTo

const val resource = "libs/ciphers/src/main/resources"

val dataSources = mapOf(
    "megumin" to ".png",
    "file" to ".pdf",
    "image" to ".jpg",
)

fun shamirCipherLong() {
    val path = "$resource/ShamirCipher"
    dataSources.forEach { (name, type) ->
        val file = "$resource/$name$type".fileToLongList()
        val method = ShamirCipherLong()
        val wrapper = EncryptWrapper("ShamirCipher", method)
        wrapper.generate()
        method.keys?.writeTo("$path/keys/", "${name}_keys.txt")
        val encrypt = wrapper.encrypt(file, true)
        encrypt.writeTo("$path/encrypt/", "${name}_encrypt$type")
        val decrypt = wrapper.decrypt(encrypt, true)
        decrypt.writeTo("$path/decrypt/", "${name}_decrypt$type")
    }
}

fun vernamCipherLong() {
    val path = "$resource/VernamCipher"
    dataSources.forEach { (name, type) ->
        val file = "$resource/$name$type".fileToLongList()
        val method = VernamCipherLong()
        val wrapper = EncryptWrapper("VernamCipher", method)
        method.keys?.writeTo("$path/keys/", "${name}_keys.txt")
        val encrypt = wrapper.encrypt(file, false)
        encrypt.writeTo("$path/encrypt/", "${name}_encrypt$type")
        val decrypt = wrapper.decrypt(encrypt, true)
        decrypt.writeTo("$path/decrypt/", "${name}_decrypt$type")
    }
}

fun rsaCipherLong() {
    val path = "$resource/RsaCipher"
    dataSources.forEach { (name, type) ->
        val file = "$resource/$name$type".fileToLongList()
        val method = RsaCipherLong()
        val wrapper = EncryptWrapper("RsaCipher", method)
        wrapper.generate()
        method.keys?.writeTo("$path/keys/", "${name}_keys.txt")
        val encrypt = wrapper.encrypt(file, false)
        encrypt.writeTo("$path/encrypt/", "${name}_encrypt$type")
        val decrypt = wrapper.decrypt(encrypt, true)
        decrypt.writeTo("$path/decrypt/", "${name}_decrypt$type")
    }
}

fun rsaCipherLongSignature() {
    val path = "$resource/RsaCipherSignature"
    dataSources.forEach { (name, type) ->
        val file = "$resource/$name$type".fileToByteArray()
        val method = RsaCipherLong()
        val wrapper = SignatureWrapper("RsaCipherSignature", method)
        wrapper.generate()
        method.keys?.writeTo("$path/keys/", "${name}_keys.txt")
        val hash = wrapper.sing(file.toList(), false)
        val verify = wrapper.verify(hash, false)
        verify.writeTo("$path/verify/", "${name}_verify.txt")
    }
}

fun elGamaliaCipherLong() {
    val path = "$resource/ElGamaliaCipher"
    dataSources.forEach { (name, type) ->
        val file = "$resource/$name$type".fileToLongList()
        val method = ElGamaliaCipherLong()
        val wrapper = EncryptWrapper("ElGamaliaCipher", method)
        wrapper.generate()
        method.keys?.writeTo("$path/keys/", "${name}_keys.txt")
        val encrypt = wrapper.encrypt(file, false)
        encrypt.writeTo("$path/encrypt/", "${name}_encrypt$type")
        val decrypt = wrapper.decrypt(encrypt, true)
        decrypt.writeTo("$path/decrypt/", "${name}_decrypt$type")
    }
}

fun elGamaliaCipherLongSignature() {
    val path = "$resource/ElGamaliaCipherSignature"
    dataSources.forEach { (name, type) ->
        val file = "$resource/$name$type".fileToByteArray()
        val method = ElGamaliaCipherSignatureLong()
        val wrapper = SignatureWrapper("ElGamaliaCipherSignature", method)
        wrapper.generate()
        method.keys?.writeTo("$path/keys/", "${name}_keys.txt")
        val hash = wrapper.sing(file.toList(), false)
        val verify = wrapper.verify(hash, false)
        verify.writeTo("$path/verify/", "${name}_verify.txt")
    }
}

fun gostElectronicSignatureLongSignature() {
    val path = "$resource/GostElectronicSignature"
    dataSources.forEach { (name, type) ->
        val file = "$resource/$name$type".fileToByteArray()
        val method = GostElectronicSignatureLong()
        val wrapper = SignatureWrapper("GostElectronicSignature", method)
        wrapper.generate()
        method.keys?.writeTo("$path/keys/", "${name}_keys.txt")
        val hash = wrapper.sing(file.toList(), true)
        val verify = wrapper.verify(hash, true)
        verify.writeTo("$path/verify/", "${name}_verify.txt")
    }
}

fun main() {
//    shamirCipherLong()
//    vernamCipherLong()
    elGamaliaCipherLong()
//    rsaCipherLong()

    elGamaliaCipherLongSignature()
//    rsaCipherLongSignature()

//    gostElectronicSignatureLongSignature()
}