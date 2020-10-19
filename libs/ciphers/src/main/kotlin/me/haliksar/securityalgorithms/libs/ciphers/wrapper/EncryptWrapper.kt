package me.haliksar.securityalgorithms.libs.ciphers.wrapper

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import me.haliksar.securityalgorithms.libs.ciphers.contract.Encrypt

class EncryptWrapper<M, E, K>(
    private val cipher: Encrypt<M, E, K>
) {

    fun generate() {
        println("Генерируем значения..")
        cipher.generate()
        println("Проверяем сгенерированные значения..")
        cipher.validate()
    }

    fun encrypt(messages: List<M>, parallel: Boolean): List<E> =
        runBlocking(Dispatchers.IO) {
            println("Начинаем шифрование..")
            if (parallel) {
                messages.parallelMap { cipher.encrypt(it) }
            } else {
                messages.map { cipher.encrypt(it) }
            }
        }

    fun decrypt(messages: List<E>, parallel: Boolean): List<M> =
        runBlocking(Dispatchers.IO) {
            println("Начинаем расшифровку..")
            if (parallel) {
                messages.parallelMap { cipher.decrypt(it) }
            } else {
                messages.map { cipher.decrypt(it) }
            }
        }

    private suspend fun <T, R> List<T>.parallelMap(block: suspend (T) -> R) =
        coroutineScope { map { async { block(it) } }.map { it.await() } }
}