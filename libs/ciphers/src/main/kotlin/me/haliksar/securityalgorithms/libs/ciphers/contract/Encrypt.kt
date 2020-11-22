package me.haliksar.securityalgorithms.libs.ciphers.contract

interface Encrypt<M, E, K> : Cipher<K> {
    fun encrypt(message: M, keys: K): E
    fun decrypt(encryptData: E, keys: K): M
}