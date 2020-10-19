package me.haliksar.securityalgorithms.libs.ciphers.wrapper

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

suspend fun <T, R> List<T>.parallelMap(block: suspend (T) -> R) =
    coroutineScope { map { async { block(it) } }.map { it.await() } }