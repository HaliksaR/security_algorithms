package me.haliksar.securityalgorithms.libs.ciphers.wrapper

interface Dumper {
    val name: String
    val dump: Boolean

    fun dumpln(message: String = "") {
        if (dump) {
            println("> ${Thread.currentThread().name} > $name $message")
        }
    }

    fun dump(message: String = "") {
        if (dump) {
            print("> ${Thread.currentThread().name} > $name $message")
        }
    }
}