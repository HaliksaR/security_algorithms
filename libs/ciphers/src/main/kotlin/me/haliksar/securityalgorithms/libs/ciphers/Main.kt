package me.haliksar.securityalgorithms.libs.ciphers

import kotlinx.coroutines.*

const val resources = "libs/ciphers/src/main/resources"

data class Resource(val name: String, val type: String) {
    val file = name + type
}

val dataSources: List<Resource> = listOf(
    Resource("megumin", ".png"),
//    Resource("file", ".pdf"),
//    Resource("image", ".jpg"),
//    Resource("Лабораторные работы", ".pdf"),
)

val jobs: CoroutineScope.(List<Resource>, Boolean) -> List<Deferred<String>>
    get() = { resources, dump ->
        mutableListOf<Deferred<String>>().apply {
            resources.forEach { resource ->
                add(async { resource.shamirCipher(dump) })
//                add(async { resource.vernamCipher(dump) })
//                add(async { resource.elGamaliaCipher(dump) })
//                add(async { resource.rsaCipher(dump) })

                add(async { resource.elGamaliaSignature(dump) })
//                add(async { resource.rsaSignature(dump) })
//                add(async { resource.gostSignature(dump) })
            }
        }
    }

fun main(): Unit = runBlocking {
    val dump = true
    launch {
        jobs(dataSources, dump)
            .awaitAll()
            .forEach { println(it) }
    }
}