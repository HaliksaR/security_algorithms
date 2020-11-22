package me.haliksar.securityalgorithms.libs.ciphers.contract

interface Cipher<K> {
    fun generate(): K
    fun validate(keys: K)
}