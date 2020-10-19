package me.haliksar.securityalgorithms.libs.ciphers.wrapper

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import me.haliksar.securityalgorithms.libs.ciphers.contract.ElectronicSignature
import me.haliksar.securityalgorithms.libs.core.fileutils.writeTo
import kotlin.system.measureTimeMillis

class SignatureWrapper<M, E, K>(
    override val name: String,
    private val cipher: ElectronicSignature<M, E, K>,
    override val dump: Boolean = true
) : Dumper {

    fun start(
        path: String,
        data: List<M>,
        dataSource: Pair<String, String>,
        singParallel: Boolean = false,
        verifyParallel: Boolean = false
    ): String {
        dumpln("START ${dataSource.first + dataSource.second}")
        val time = measureTimeMillis {
            generate()
            dump()
            cipher.keys?.writeTo("$path/keys/", "${dataSource.first}_keys.txt", dump)
            val hash = sing(data, singParallel)
            val verify = verify(hash, verifyParallel)
            dump()
            verify.writeTo("$path/verify/", "${dataSource.first}_verify.txt", dump)
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

    fun sing(messages: List<M>, parallel: Boolean): List<E> =
        runBlocking(Dispatchers.IO) {
            dumpln("Начинаем подпись ключей...")
            if (parallel) {
                messages.parallelMap { cipher.sign(it) }
            } else {
                messages.map { cipher.sign(it) }
            }
        }

    fun verify(hash: List<E>, parallel: Boolean): Boolean =
        runBlocking(Dispatchers.IO) {
            dumpln("Начинаем расшифровку...")
            if (parallel) {
                hash.parallelMap { cipher.verify(it) }.none { !it }
            } else {
                hash.map { cipher.verify(it) }.none { !it }
            }.also {
                if (it) {
                    dumpln("Верификация прошла успешно!")
                } else {
                    dumpln("Ошибка верификации")
                }
            }
        }
}