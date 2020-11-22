package me.haliksar.securityalgorithms.libs.ciphers.wrapper

import com.github.ajalt.mordant.terminal.TextColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.haliksar.securityalgorithms.libs.ciphers.Resource
import me.haliksar.securityalgorithms.libs.ciphers.contract.ElectronicSignature
import me.haliksar.securityalgorithms.libs.core.fileutils.fileToListObject
import me.haliksar.securityalgorithms.libs.core.fileutils.writeByteTo
import me.haliksar.securityalgorithms.libs.core.fileutils.writeTo
import kotlin.system.measureTimeMillis

class SignatureWrapper<M : Any, K : Any, S : Any>(
    override val name: String,
    private val cipher: ElectronicSignature<M, K, S>,
    override val dump: Boolean = true,
    override val resource: Resource,
) : Dumper() {

    suspend fun start(
        path: String,
        data: List<M>,
        singParallel: Boolean = false,
        verifyParallel: Boolean = false,
    ): String = withContext(Dispatchers.IO) {
        dumpln("START ${resource.file}")
        val time = measureTimeMillis {
            val keys = generate().also {
                dump()
                it.writeTo("$path/keys/", "${resource.name}.keys", dump)
            }

            sing(data, singParallel, keys).also {
                dump()
                it.writeByteTo("$path/signature/", "${resource.name}.signature", dump)
            }

            dump()
            val signatures = "$path/signature/${resource.name}.signature".fileToListObject<List<S>>()

            verify(data, signatures, verifyParallel, keys).also {
                dump()
                it.writeTo("$path/verify/", "${resource.name}.verify", dump)
            }
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

    private suspend fun sing(messages: List<M>, parallel: Boolean, keys: K): List<S> {
        dumpln("Sign keys...")
        return if (parallel) {
            messages.parallelMap { cipher.sign(it, keys) }
        } else {
            messages.map { cipher.sign(it, keys) }
        }
    }

    private suspend fun verify(messages: List<M>, hash: List<S>, parallel: Boolean, keys: K): Boolean {
        dumpln("Verify...")
        return if (parallel) {
            messages.zip(hash).parallelMap { cipher.verify(it.first, it.second, keys) }.none { !it }
        } else {
            messages.zip(hash).map { cipher.verify(it.first, it.second, keys) }.none { !it }
        }.also {
            if (it) {
                dumpln("Verify done!", TextColors.green)
            } else {
                dumpln("Error verify", TextColors.red)
            }
        }
    }
}