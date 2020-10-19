package me.haliksar.securityalgorithms.libs.ciphers.wrapper

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import me.haliksar.securityalgorithms.libs.ciphers.contract.Encrypt
import me.haliksar.securityalgorithms.libs.core.fileutils.writeTo
import kotlin.system.measureTimeMillis

class EncryptWrapper<M, E, K>(
    override val name: String,
    private var cipher: Encrypt<M, E, K>,
    override val dump: Boolean = true
) : Dumper {


    fun start(
        path: String,
        data: List<M>,
        dataSource: Pair<String, String>,
        encryptParallel: Boolean = false,
        decryptParallel: Boolean = false
    ): String {
        dumpln("START ${dataSource.first + dataSource.second}")
        val time = measureTimeMillis {
            generate()
            dump()
            cipher.keys?.writeTo("$path/keys/", "${dataSource.first}_keys.txt", dump)
            val encrypt = encrypt(data, encryptParallel)
            dump()
            encrypt.writeTo("$path/encrypt/", "${dataSource.first}_encrypt${dataSource.second}", dump)
            val decrypt = decrypt(encrypt, decryptParallel)
            dump()
            decrypt.writeTo("$path/decrypt/", "${dataSource.first}_decrypt${dataSource.second}", dump)
        }
        dumpln("EXIT ${dataSource.first + dataSource.second}")
        return "$name\t${dataSource.first + dataSource.second}\tTOTAL TIME $time ms"
    }

    fun generate() {
        dumpln("Генерируем значения...")
        cipher.generate()
        dumpln("Проверяем сгенерированные значения...")
        cipher.validate()
    }

    fun encrypt(messages: List<M>, parallel: Boolean): List<E> =
        runBlocking(Dispatchers.IO) {
            dumpln("Начинаем шифрование...")
            if (parallel) {
                messages.parallelMap { cipher.encrypt(it) }
            } else {
                messages.map { cipher.encrypt(it) }
            }
        }

    fun decrypt(messages: List<E>, parallel: Boolean): List<M> =
        runBlocking(Dispatchers.IO) {
            dumpln("Начинаем расшифровку...")
            if (parallel) {
                messages.parallelMap { cipher.decrypt(it) }
            } else {
                messages.map { cipher.decrypt(it) }
            }
        }
}