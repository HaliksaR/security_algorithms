package me.haliksar.securityalgorithms.libs.ciphers.contract

interface Cipher<K> {
    val keys: K?
    fun generate()
    fun validate()
}