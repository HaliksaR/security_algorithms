package me.haliksar.securityalgorithms.libs.ciphers

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

const val resource = "libs/ciphers/src/main/resources"

val dataSources: Map<String, String> = mapOf(
    "megumin" to ".png",
    "file" to ".pdf",
    "image" to ".jpg",
    "Лабораторные работы" to ".pdf",
)

val CoroutineScope.jobs: (Map<String, String>, Boolean) -> List<Deferred<String>>
    get() = { data, dump ->
        mutableListOf<Deferred<String>>().apply {
            data.forEach { pair ->
//                add(async { shamirCipherLong(pair.toPair(), dump) })
//                add(async { vernamCipherLong(pair.toPair(), dump) })
//                add(async { elGamaliaCipherLong(pair.toPair(), dump) })
//                add(async { rsaCipherLong(pair.toPair(), dump) })
//
//                add(async { elGamaliaLongSignature(pair.toPair(), dump) })
//                add(async { rsaLongSignature(pair.toPair(), dump) })
//                add(async { gostLongSignature(pair.toPair(), dump) })
            }
        }
    }

fun main(): Unit = runBlocking(Dispatchers.IO) {
    val dump = true
    val times = mutableListOf<String>()
    jobs(dataSources, dump).forEach { times.add(it.await()) }
    times.forEach { println(it) }
}