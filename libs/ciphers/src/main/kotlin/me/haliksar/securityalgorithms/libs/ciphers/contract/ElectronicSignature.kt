package me.haliksar.securityalgorithms.libs.ciphers.contract


interface ElectronicSignature<M, K, S> : Cipher<K> {
    fun sign(message: M, keys: K): S
    fun verify(message: M, signature: S, keys: K): Boolean
}