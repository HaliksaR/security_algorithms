package me.haliksar.securityalgorithms.libs.ciphers.wrapper

import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.terminal.TextColors
import me.haliksar.securityalgorithms.libs.ciphers.Resource

abstract class Dumper {
    abstract val name: String
    abstract val dump: Boolean
    abstract val resource: Resource

    private val mordant: Terminal = Terminal()

    private fun message(message: String) =
        "> ${Thread.currentThread().name} > [$name file:${resource.file}] $message"

    protected fun dumpln(message: String = "", tc: TextColors? = null) {
        if (dump) {
            if (tc != null) {
                mordant.println(tc(message(message)))
            } else {
                mordant.println(message(message))
            }
        }
    }

    protected fun dump(message: String = "", tc: TextColors? = null) {
        if (dump) {
            if (tc != null) {
                mordant.print(tc(message(message)))
            } else {
                mordant.print(message(message))
            }
        }
    }
}