package me.haliksar.securityalgorithms.libs.ciphers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

const val resource = "libs/ciphers/src/main/resources"

fun <K, V> Map<K, V>.asFlow(): Flow<Pair<K, V>> = flow {
    forEach { emit(it.key to it.value) }
}

val dataSources: Flow<Pair<String, String>> = mapOf(
    "megumin" to ".png",
    "file" to ".pdf",
    "image" to ".jpg",
).asFlow()

fun main(): Unit = runBlocking {
    dataSources.collect {
        shamirCipherLong(it, false)
        vernamCipherLong(it, false)
        elGamaliaCipherLong(it, false)
        rsaCipherLong(it, false)

        elGamaliaCipherLongSignature(it, false)
        rsaCipherLongSignature(it, false)
        gostElectronicSignatureLongSignature(it, false)
    }
}