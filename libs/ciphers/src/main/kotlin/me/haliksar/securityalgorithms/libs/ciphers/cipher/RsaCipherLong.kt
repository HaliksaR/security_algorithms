package me.haliksar.securityalgorithms.libs.ciphers.cipher

import me.haliksar.securityalgorithms.libs.ciphers.contract.ElectronicSignature
import me.haliksar.securityalgorithms.libs.ciphers.contract.Encrypt
import me.haliksar.securityalgorithms.libs.core.hashutils.SHA_256B
import me.haliksar.securityalgorithms.libs.core.prime.multiplicativeInverse
import me.haliksar.securityalgorithms.libs.core.prime.mutuallyPrime
import me.haliksar.securityalgorithms.libs.core.prime.shortPrimeNumber
import me.haliksar.securityalgorithms.libs.modexp.long.modExpRec
import kotlin.properties.Delegates

class RsaCipherLong :
    Encrypt<Long, Long, RsaCipherLong.Keys>,
    ElectronicSignature<Byte, RsaCipherLong.HashData, RsaCipherLong.Keys> {

    data class Keys(var publicKey: Pair<Long, Long>, var privateKey: Pair<Long, Long>)

    data class HashData(val m: Byte, val s: Long)

    override var keys: Keys? = null
    private var keysData: Keys by Delegates.notNull()

    override fun generate() {
        val p = Long.shortPrimeNumber
        val q = Long.shortPrimeNumber
        val n = p * q // модуль
        val f = (p - 1L) * (q - 1L) // функция Эйлера
        val e = Long.mutuallyPrime(f) // открытая экспонента, простая из чисел Ферма
        val d = Long.multiplicativeInverse(e, f)  // Секретная экспонента
        keysData = Keys(publicKey = Pair(e, n), privateKey = Pair(d, n))
        keys = keysData
    }

    override fun encrypt(message: Long): Long {
        val (e, n) = keysData.publicKey
        return message.modExpRec(e, n)
    }

    override fun decrypt(encryptData: Long): Long {
        val (d, n) = keysData.privateKey
        return encryptData.modExpRec(d, n)
    }

    override fun sign(message: Byte): HashData {
        val (c, n) = keysData.privateKey
        val h = message.SHA_256B
        val s = h.toLong().modExpRec(c, n)
        return HashData(message, s)
    }

    override fun verify(data: HashData): Boolean {
        val (d, n) = keysData.publicKey
        val m = data.m
        val s = data.s
        val h = m.SHA_256B
        val e = s.modExpRec(d, n).toByte()
        return e == h
    }

    override fun validate() {}
}