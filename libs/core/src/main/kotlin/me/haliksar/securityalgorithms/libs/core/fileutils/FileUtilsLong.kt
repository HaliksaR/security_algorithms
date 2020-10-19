package me.haliksar.securityalgorithms.libs.core.fileutils

import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.nio.file.Files

fun String.fileToLongList(dump: Boolean = true): List<Long> {
    val list = mutableListOf<Long>()
    for (element in fileToByteArray(dump))
        list.add(element.toLong())
    return list.toList()
}

fun String.fileToByteArray(dump: Boolean = true): ByteArray {
    if (dump) println("Считываем файл '${replace('/', '\\')}'...")
    val file = File(this)
    return Files.readAllBytes(file.toPath())
}

fun String.fileToByteList(dump: Boolean = true): List<Byte> =
    fileToByteArray(dump).toList()

fun List<Number>.writeTo(dir: String, name: String, dump: Boolean = true) {
    val dir = File(dir)
    if (!dir.exists()) {
        dir.mkdirs()
    }
    if (dump) println("Создаем файл '$dir\\$name'...")
    val bytes = ByteArray(size)
    for (i in indices) {
        bytes[i] = get(i).toByte()
    }
    FileOutputStream("${dir.absolutePath}\\$name").use { stream ->
        stream.write(bytes)
    }
}

fun Any.writeTo(dir: String, name: String, dump: Boolean = true) {
    if (this is List<*> && this.last() is Number) {
        return (this as List<Number>).writeTo(dir, name, dump)
    }
    val dir = File(dir)
    if (!dir.exists()) {
        dir.mkdirs()
    }
    if (dump) println("Создаем файл '$dir\\$name'...")
    FileWriter("${dir.absolutePath}\\$name").use {
        it.write(this.toString())
    }
}