package me.haliksar.securityalgorithms.libs.ciphers

import me.haliksar.securityalgorithms.libs.ciphers.long.ShamirCipherLong
import me.haliksar.securityalgorithms.libs.core.fileutils.toFile
import me.haliksar.securityalgorithms.libs.core.fileutils.toLongList

const val path = "libs/ciphers/src/main/resources"

val dataSources = mapOf(
    "megumin" to ".png",
    "file" to ".pdf",
    "image" to ".jpg",
)

@ExperimentalUnsignedTypes
fun main() {
    dataSources.forEach { (name, type) ->
        val image = "$path/$name$type".toLongList()
        val method = ShamirCipherLong()
        val wrapper = EncryptWrapper(method)
        val encrypt = wrapper.encrypt(image)
        encrypt toFile "$path/${name}_encrypt$type"
        val decrypt = wrapper.decrypt(encrypt)
        decrypt toFile "$path/${name}_decrypt$type"
    }
}