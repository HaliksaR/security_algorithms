package me.haliksar.securityalgorithms.libs.core.fileutils

import java.io.FileOutputStream

@ExperimentalUnsignedTypes
fun String.toULongList(): List<ULong> {
    val list = mutableListOf<ULong>()
    for (element in fileToByteArray())
        list.add(element.toULong())
    return list.toList()
}

@JvmName("toFileULong")
@ExperimentalUnsignedTypes
infix fun List<ULong>.writeTo(name: String) {
    println("Создаем файл '$name'..")
    val bytes = ByteArray(size)
    for (i in indices) {
        bytes[i] = get(i).toByte()
    }
    FileOutputStream(name).use { stream -> stream.write(bytes) }
}
