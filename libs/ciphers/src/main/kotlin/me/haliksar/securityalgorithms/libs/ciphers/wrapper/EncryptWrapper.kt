package me.haliksar.securityalgorithms.libs.ciphers.wrapper

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import me.haliksar.securityalgorithms.libs.ciphers.contract.Encrypt

class EncryptWrapper<M, E, K>(
    name: String,
    private val cipher: Encrypt<M, E, K>,
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

    fun encrypt(messages: List<M>, parallel: Boolean): List<E> =
        runBlocking(Dispatchers.IO) {
            dump("Начинаем шифрование..")
            if (parallel) {
                messages.parallelMap { cipher.encrypt(it) }
            } else {
                messages.map { cipher.encrypt(it) }
            }
        }

    fun decrypt(messages: List<E>, parallel: Boolean): List<M> =
        runBlocking(Dispatchers.IO) {
            dump("Начинаем расшифровку..")
            if (parallel) {
                messages.parallelMap { cipher.decrypt(it) }
            } else {
                messages.map { cipher.decrypt(it) }
            }
        }

    private suspend fun <T, R> List<T>.parallelMap(block: suspend (T) -> R) =
        coroutineScope { map { async { block(it) } }.map { it.await() } }
}