package me.haliksar.securityalgorithms.libs.ciphers.wrapper

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import me.haliksar.securityalgorithms.libs.ciphers.contract.ElectronicSignature

class SignatureWrapper<M, E, K>(
    name: String,
    private val cipher: ElectronicSignature<M, E, K>,
    private val dump: Boolean = true
) {

    private fun dump(message: String) {
        if (dump) {
            println(message)
        }
    }

    init {
        dump(name)
    }

    fun generate() {
        dump("Генерируем значения..")
        cipher.generate()
        dump("Проверяем сгенерированные значения..")
        cipher.validate()
    }

    fun sing(messages: List<M>, parallel: Boolean): List<E> =
        runBlocking(Dispatchers.IO) {
            dump("Начинаем подпись ключей..")
            if (parallel) {
                messages.parallelMap { cipher.sign(it) }
            } else {
                messages.map { cipher.sign(it) }
            }
        }

    fun verify(hash: List<E>, parallel: Boolean): Boolean =
        runBlocking(Dispatchers.IO) {
            dump("Начинаем расшифровку..")
            if (parallel) {
                hash.parallelMap { cipher.verify(it) }.none { !it }
            } else {
                hash.map { cipher.verify(it) }.none { !it }
            }.also {
                if (it) {
                    dump("Верификация прошла успешно!")
                } else {
                    dump("Ошибка верификации")
                }
            }
        }

    private suspend fun <T, R> List<T>.parallelMap(block: suspend (T) -> R) =
        coroutineScope { map { async { block(it) } }.map { it.await() } }
}