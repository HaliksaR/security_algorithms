package me.haliksar.securityalgorithms.libs.ciphers

import me.haliksar.securityalgorithms.libs.ciphers.long.ShamirCipherLong
import me.haliksar.securityalgorithms.libs.ciphers.ulong.ShamirCipherULong
import java.io.FileWriter

const val path = "libs/ciphers/src/main/resources"

val dataSources = mapOf(
    "megumin" to ".png",
    "file" to ".pdf",
    "image" to ".jpg",
)

infix fun ShamirCipherLong.Keys.writeTo(name: String) {
    println("Создаем файл '$name'..")
    FileWriter(name).use {
        it.write(this.toString())
    }
}

@ExperimentalUnsignedTypes
infix fun ShamirCipherULong.Keys.writeTo(name: String) {
    println("Создаем файл '$name'..")
    FileWriter(name).use {
        it.write(this.toString())
    }
}

@ExperimentalUnsignedTypes
fun main() {
    dataSources.forEach { (name, type) ->
/*        val image = "$path/$name$type".toLongList()
        val method = ShamirCipherLong()
        val wrapper = EncryptWrapper(method)
        val keys = wrapper.generate()
        keys writeTo "$path/${name}_keys.txt"
        val encrypt = wrapper.encrypt(image)
        encrypt writeTo "$path/${name}_encrypt$type"
        val decrypt = wrapper.decrypt(encrypt)
        decrypt writeTo "$path/${name}_decrypt$type"
*/
        val mod = 12
        val modMutual = 7 * 7 * 7 * 7 * 7 * 7 * 7 * 7
        val modMultiInverse = modMutual % mod
        println(modMultiInverse % mod)
    }
}