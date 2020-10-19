package me.haliksar.securityalgorithms.libs.ciphers

import me.haliksar.securityalgorithms.libs.ciphers.cipher.ElGamaliaCipherLong
import me.haliksar.securityalgorithms.libs.ciphers.cipher.ShamirCipherLong
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

@ExperimentalUnsignedTypes
fun main() {
    val path1 = "$path/ShamirCipher"
    dataSources.forEach { (name, type) ->
        val file = "$path/$name$type".fileToLongList()
        val method = ShamirCipherLong()
        val wrapper = EncryptWrapper(method)
        wrapper.generate()
        method.keys?.writeTo("$path1/keys/", "${name}_keys.txt")
        val encrypt = wrapper.encrypt(file)
        encrypt.writeTo("$path1/encrypt/", "${name}_encrypt$type")
        val decrypt = wrapper.decrypt(encrypt)
        decrypt.writeTo("$path1/decrypt/", "${name}_decrypt$type")
    }

    val path2 = "$path/ElGamaliaCipher"
    dataSources.forEach { (name, type) ->
        val file = "$path/$name$type".fileToByteArray()
        val method = ElGamaliaCipherLong()
        val wrapper = SignatureWrapper(method)
        wrapper.generate()
        method.keys?.writeTo("$path2/keys/", "${name}_keys.txt")
        val hash = wrapper.sing(file.toList())
        if (wrapper.verify(hash)) {
            println("Верификация прошла успешно!")
        } else {
            println("Ошибка верификации")
        }
    }
}