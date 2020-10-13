package me.haliksar.securityalgorithms.libs.core.fileutils

import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files

fun String.toLongList(): List<Long> {
    val list = mutableListOf<Long>()
    for (element in toByteArray())
        list.add(element.toLong())
    return list.toList()
}

fun String.toByteArray(): ByteArray {
    println("Считываем файл..")
    val file = File(this)
    return Files.readAllBytes(file.toPath())
}

@JvmName("toFileLong")
infix fun List<Long>.writeTo(name: String) {
    println("Создаем файл '$name'..")
    val bytes = ByteArray(size)
    for (i in indices) {
        bytes[i] = get(i).toByte()
    }
    FileOutputStream(name).use { stream -> stream.write(bytes) }
}