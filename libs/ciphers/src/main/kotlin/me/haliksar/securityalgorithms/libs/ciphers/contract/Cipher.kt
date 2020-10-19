package me.haliksar.securityalgorithms.libs.ciphers.contract

interface Cipher<K> {
    val keys: K?
    val keysData: K
    fun generate()
    fun validate()
}