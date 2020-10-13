package me.haliksar.securityalgorithms.libs.ciphers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking

class EncryptWrapper<M, E, K>(
    private val cipher: Encrypt<M, E, K>
) {

    fun generate(): K {
        println("Генерируем значения..")
        val keys = cipher.generate()
        println("Проверяем сгенерированные значения..")
        cipher.validate()
        return keys
    }

    fun encrypt(messages: List<M>): List<E> =
        runBlocking(Dispatchers.IO) {
            println("Начинаем шифрование..")
            messages.parallelMap { cipher.encrypt(it) }
        }

    fun decrypt(messages: List<E>): List<M> =
        runBlocking(Dispatchers.IO) {
            println("Начинаем расшифровку..")
            messages.parallelMap { cipher.decrypt(it) }
        }

    private suspend fun <T, R> List<T>.parallelMap(block: suspend (T) -> R) =
        coroutineScope { map { async { block(it) } }.map { it.await() } }
}