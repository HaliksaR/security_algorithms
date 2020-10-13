package me.haliksar.securityalgorithms.libs.ciphers

interface Encrypt<M, E> : Cipher {
    fun encrypt(message: M): E
    fun decrypt(encryptData: E): M
}