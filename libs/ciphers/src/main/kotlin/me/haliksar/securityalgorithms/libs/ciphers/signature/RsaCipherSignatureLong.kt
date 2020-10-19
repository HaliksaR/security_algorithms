package me.haliksar.securityalgorithms.libs.ciphers.signature

import me.haliksar.securityalgorithms.libs.ciphers.contract.ElectronicSignature
import me.haliksar.securityalgorithms.libs.core.hashutils.SHA_256B
import me.haliksar.securityalgorithms.libs.core.prime.multiplicativeInverse
import me.haliksar.securityalgorithms.libs.core.prime.mutuallyPrime
import me.haliksar.securityalgorithms.libs.core.prime.randomPrimeNumber
import me.haliksar.securityalgorithms.libs.modexp.long.modExpRec
import kotlin.properties.Delegates

class RsaCipherSignatureLong :
    ElectronicSignature<Byte, RsaCipherSignatureLong.HashData, RsaCipherSignatureLong.Keys> {

    data class HashData(val m: Byte, val s: Long)

    data class Keys(var publicKey: Pair<Long, Long>, var privateKey: Pair<Long, Long>)

    override var keys: Keys? = null
    override var keysData: Keys by Delegates.notNull()

    override fun generate() {
        val p = Long.randomPrimeNumber
        val q = Long.randomPrimeNumber
        val n = p * q // модуль
        val f = (p - 1L) * (q - 1L) // функция Эйлера
        val e = Long.mutuallyPrime(f) // открытая экспонента, простая из чисел Ферма
        val d = Long.multiplicativeInverse(e, f)  // Секретная экспонента
        keysData = Keys(publicKey = Pair(e, n), privateKey = Pair(d, n))
        keys = keysData
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