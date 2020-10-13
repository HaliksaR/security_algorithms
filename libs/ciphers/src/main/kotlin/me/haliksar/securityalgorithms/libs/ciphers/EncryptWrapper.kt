package me.haliksar.securityalgorithms.libs.ciphers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking

class EncryptWrapper<M, E>(
    private val cipher: Encrypt<M, E>
) {

    fun encrypt(messages: List<M>): List<E> =
        runBlocking(Dispatchers.IO) {
            println("Генерируем значения..")
            cipher.generate()
            println("Проверяем сгенерированные значения..")
            cipher.validate()
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