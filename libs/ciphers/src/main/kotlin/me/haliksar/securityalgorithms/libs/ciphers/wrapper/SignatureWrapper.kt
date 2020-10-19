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

    fun sing(messages: List<M>, parallel: Boolean): List<E> =
        runBlocking(Dispatchers.IO) {
            println("Начинаем подпись ключей..")
            if (parallel) {
                messages.parallelMap { cipher.sign(it) }
            } else {
                messages.map { cipher.sign(it) }
            }
        }

    fun verify(hash: List<E>, parallel: Boolean): Boolean =
        runBlocking(Dispatchers.IO) {
            println("Начинаем расшифровку..")
            if (parallel) {
                hash.parallelMap { cipher.verify(it) }.none { !it }
            } else {
                hash.map { cipher.verify(it) }.none { !it }
            }
        }

    private suspend fun <T, R> List<T>.parallelMap(block: suspend (T) -> R) =
        coroutineScope { map { async { block(it) } }.map { it.await() } }
}