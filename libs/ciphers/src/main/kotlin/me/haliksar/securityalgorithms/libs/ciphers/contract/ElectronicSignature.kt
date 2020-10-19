package me.haliksar.securityalgorithms.libs.ciphers.contract


interface ElectronicSignature<M, T, K> : Cipher<K> {
    fun sign(message: M): T
    fun verify(data: T): Boolean
}