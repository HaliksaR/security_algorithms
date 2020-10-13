package me.haliksar.securityalgorithms.libs.ciphers

interface Encrypt<M, E, K> : Cipher<K> {
    fun encrypt(message: M): E
    fun decrypt(encryptData: E): M
}