package me.haliksar.securityalgorithms.libs.core.fileutils

import java.io.*
import java.nio.file.Files

fun String.fileToLongList(dump: Boolean = true): List<Long> {
    val list = mutableListOf<Long>()
    for (element in fileToByteArray(dump))
        list.add(element.toLong())
    return list.toList()
}

fun String.fileToByteArray(dump: Boolean = true): ByteArray {
    if (dump) println("Read file '${replace('/', '\\')}'...")
    val file = File(this)
    return Files.readAllBytes(file.toPath())
}

fun String.fileToByteList(dump: Boolean = true): List<Byte> =
    fileToByteArray(dump).toList()

private fun createFile(dir: String): File {
    val dir = File(dir)
    if (!dir.exists()) {
        dir.mkdirs()
    }
    return dir
}

fun List<Number>.numberWriteTo(dir: String, name: String, dump: Boolean = true) {
    val file = createFile(dir)
    if (dump) println("Create file '$file\\$name'...")
    val bytes = ByteArray(size)
    for (i in indices) {
        bytes[i] = get(i).toByte()
    }
    FileOutputStream("${file.absolutePath}\\$name").use { stream ->
        stream.write(bytes)
    }
}

fun List<Any>.anyWriteTo(dir: String, name: String, dump: Boolean = true) {
    val file = createFile(dir)
    if (dump) println("Create file '$file\\$name'...")
    FileWriter("${file.absolutePath}\\$name").use {
        it.write(this.joinToString("\n"))
    }
}

fun Any.writeTo(dir: String, name: String, dump: Boolean = true) {
    if (this is List<*>) {
        return when (this.last()) {
            is Number -> (this as List<Number>).numberWriteTo(dir, name, dump)
            else -> (this as List<Any>).anyWriteTo(dir, name, dump)
        }
    }
    val file = createFile(dir)
    if (dump) println("Create file '$file\\$name'...")
    FileWriter("${file.absolutePath}\\$name").use {
        it.write(this.toString())
    }
}

fun Any.writeByteTo(dir: String, name: String, dump: Boolean = true) {
    val file = createFile(dir)
    val bos = ByteArrayOutputStream()
    ObjectOutputStream(bos).use {
        it.writeObject(this)
    }
    if (dump) println("Create file '$file\\$name'...")
    FileOutputStream("${file.absolutePath}\\$name").use { stream ->
        stream.write(bos.toByteArray())
    }
}

fun <T : Any> String.fileToListObject(dump: Boolean = true): T {
    if (dump) println("Read file '${replace('/', '\\')}'...")
    val file = File(this)
    val obj = ObjectInputStream(file.inputStream()).use {
        it.readObject()
    }
    return obj as T
}