package me.haliksar.securityalgorithms.libs.ciphers

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

const val resource = "libs/ciphers/src/main/resources"

fun <K, V> Map<K, V>.asFlow(): Flow<Pair<K, V>> = flow {
    forEach { emit(it.key to it.value) }
}

val dataSources: Flow<Pair<String, String>> = mapOf(
    "megumin" to ".png",
    "file" to ".pdf",
    "image" to ".jpg",
    "Лабораторные работы" to ".pdf",
).asFlow().flowOn(Dispatchers.IO)

val CoroutineScope.jobs: (Pair<String, String>, Boolean) -> List<Deferred<Unit>>
    get() = { data, dump ->
        listOf(
            async { shamirCipherLong(data, dump) },
            async { vernamCipherLong(data, dump) },
            async { elGamaliaCipherLong(data, dump) },
            async { rsaCipherLong(data, dump) },

            async { elGamaliaLongSignature(data, dump) },
            async { rsaLongSignature(data, dump) },
            async { gostLongSignature(data, dump) }
        )
    }

fun main(): Unit = runBlocking(Dispatchers.IO) {
    val dump = true
    dataSources.collect {
        jobs(it, dump).forEach { it.await() }
    }
}