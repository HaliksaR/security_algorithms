package me.haliksar.securityalgorithms.libs.ciphers.wrapper

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import me.haliksar.securityalgorithms.libs.ciphers.contract.ElectronicSignature

class SignatureWrapper<M, E, K>(
    private val cipher: ElectronicSignature<M, E, K>
) {

    fun generate() {
        println("Генерируем значения..")
        cipher.generate()
        println("Проверяем сгенерированные значения..")
        cipher.validate()
    }

    fun sing(messages: List<M>): List<E> =
        runBlocking(Dispatchers.IO) {
            println("Начинаем подпись ключей..")
            messages.map { cipher.sign(it) }
        }

    fun verify(hash: List<E>): Boolean =
        runBlocking(Dispatchers.IO) {
            println("Начинаем расшифровку..")
            hash.parallelMap { cipher.verify(it) }.none { !it }
        }

    private suspend fun <T, R> List<T>.parallelMap(block: suspend (T) -> R) =
        coroutineScope { map { async { block(it) } }.map { it.await() } }
}