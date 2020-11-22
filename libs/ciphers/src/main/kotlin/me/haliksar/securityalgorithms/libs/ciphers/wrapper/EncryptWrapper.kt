package me.haliksar.securityalgorithms.libs.ciphers.wrapper

import com.github.ajalt.mordant.terminal.TextColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.haliksar.securityalgorithms.libs.ciphers.Resource
import me.haliksar.securityalgorithms.libs.ciphers.contract.Encrypt
import me.haliksar.securityalgorithms.libs.ciphers.resources
import me.haliksar.securityalgorithms.libs.core.fileutils.writeTo
import java.io.File
import kotlin.system.measureTimeMillis

class EncryptWrapper<M : Any, E : Any, K : Any>(
    override val name: String,
    private val cipher: Encrypt<M, E, K>,
    override val dump: Boolean = true,
    override val resource: Resource,
) : Dumper() {

    suspend fun start(
        path: String,
        data: List<M>,
        encryptParallel: Boolean = false,
        decryptParallel: Boolean = false,
    ): String = withContext(Dispatchers.IO) {
        dumpln("START ${resource.file}")
        val time = measureTimeMillis {
            val keys = generate().also {
                dump()
                it.writeTo("$path/keys/", "${resource.name}.keys", dump)
            }

            val encrypt = encrypt(data, encryptParallel, keys).also {
                dump()
                it.writeTo("$path/encrypt/", "${resource.name}_encrypt${resource.type}", dump)
            }

            decrypt(encrypt, decryptParallel, keys).also {
                dump()
                it.writeTo("$path/decrypt/", "${resource.name}_decrypt${resource.type}", dump)
            }
            compareFiles(path)
        }
        dumpln("EXIT")
        "$name\t${resource.file}\tTOTAL TIME $time ms"
    }

    private fun generate(): K {
        dumpln("Generate values...")
        return cipher.generate().also {
            dumpln("Validate values...")
            cipher.validate(it)
        }
    }

    private suspend fun encrypt(messages: List<M>, parallel: Boolean, keys: K): List<E> {
        dumpln("Encrypt...")
        return if (parallel) {
            messages.parallelMap { cipher.encrypt(it, keys) }
        } else {
            messages.map { cipher.encrypt(it, keys) }
        }
    }

    private suspend fun decrypt(messages: List<E>, parallel: Boolean, keys: K): List<M> {
        dumpln("Decrypt...")
        return if (parallel) {
            messages.parallelMap { cipher.decrypt(it, keys) }
        } else {
            messages.map { cipher.decrypt(it, keys) }
        }
    }

    private fun compareFiles(path: String) {
        val decrypt = File("$path/decrypt/", "${resource.name}_decrypt${resource.type}").readBytes()
        val orig = File("$resources/${resource.file}").readBytes()
        if (decrypt.contentEquals(orig)) {
            dumpln("Files equals!", TextColors.green)
        } else {
            dumpln("Files not equals!", TextColors.red)
        }
    }
}