package me.haliksar.securityalgorithms.libs.ciphers

interface Cipher<K> {
    fun generate(): K
    fun validate()
}