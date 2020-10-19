package me.haliksar.securityalgorithms.libs.ciphers

import me.haliksar.securityalgorithms.libs.ciphers.cipher.ElGamaliaCipherLong
import me.haliksar.securityalgorithms.libs.ciphers.cipher.ShamirCipherLong
import me.haliksar.securityalgorithms.libs.ciphers.cipher.VernamCipherLong
import me.haliksar.securityalgorithms.libs.ciphers.wrapper.EncryptWrapper
import me.haliksar.securityalgorithms.libs.ciphers.wrapper.SignatureWrapper
import me.haliksar.securityalgorithms.libs.core.fileutils.fileToByteArray
import me.haliksar.securityalgorithms.libs.core.fileutils.fileToLongList
import me.haliksar.securityalgorithms.libs.core.fileutils.writeTo
import java.io.File
import java.io.FileWriter

const val path = "libs/ciphers/src/main/resources"

val dataSources = mapOf(
    "megumin" to ".png",
    "file" to ".pdf",
    "image" to ".jpg",
)

fun Any.writeTo(dir: String, name: String) {
    val dir = File(dir)
    if (!dir.exists()) {
        dir.mkdirs()
    }
    println("Создаем файл '$name'..")
    FileWriter("${dir.absolutePath}/$name").use {
        it.write(this.toString())
    }
}

fun shamirCipherLong() {
    val path1 = "$path/ShamirCipher"
    dataSources.forEach { (name, type) ->
        val file = "$path/$name$type".fileToLongList()
        val method = ShamirCipherLong()
        val wrapper = EncryptWrapper(method)
        wrapper.generate()
        method.keys?.writeTo("$path1/keys/", "${name}_keys.txt")
        val encrypt = wrapper.encrypt(file, true)
        encrypt.writeTo("$path1/encrypt/", "${name}_encrypt$type")
        val decrypt = wrapper.decrypt(encrypt, true)
        decrypt.writeTo("$path1/decrypt/", "${name}_decrypt$type")
    }
}

fun vernamCipherLong() {
    val path1 = "$path/VernamCipher"
    dataSources.forEach { (name, type) ->
        val file = "$path/$name$type".fileToLongList()
        val method = VernamCipherLong()
        val wrapper = EncryptWrapper(method)
        method.keys?.writeTo("$path1/keys/", "${name}_keys.txt")
        val encrypt = wrapper.encrypt(file, false)
        encrypt.writeTo("$path1/encrypt/", "${name}_encrypt$type")
        val decrypt = wrapper.decrypt(encrypt, true)
        decrypt.writeTo("$path1/decrypt/", "${name}_decrypt$type")
    }
}

fun elGamaliaCipherLong() {
    val path2 = "$path/ElGamaliaCipher"
    dataSources.forEach { (name, type) ->
        val file = "$path/$name$type".fileToByteArray()
        val method = ElGamaliaCipherLong()
        val wrapper = SignatureWrapper(method)
        wrapper.generate()
        method.keys?.writeTo("$path2/keys/", "${name}_keys.txt")
        val hash = wrapper.sing(file.toList(), false)
        if (wrapper.verify(hash, true)) {
            println("Верификация прошла успешно!")
        } else {
            println("Ошибка верификации")
        }
    }
}

@ExperimentalUnsignedTypes
fun main() {
/*    shamirCipherLong()
    elGamaliaCipherLong()*/
    vernamCipherLong()
}