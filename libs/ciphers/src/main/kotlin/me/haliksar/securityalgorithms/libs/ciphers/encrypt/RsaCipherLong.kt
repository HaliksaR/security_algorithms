package me.haliksar.securityalgorithms.libs.ciphers.encrypt

import me.haliksar.securityalgorithms.libs.ciphers.contract.Encrypt
import me.haliksar.securityalgorithms.libs.core.prime.multiplicativeInverse
import me.haliksar.securityalgorithms.libs.core.prime.mutuallyPrime
import me.haliksar.securityalgorithms.libs.core.prime.shortPrimeNumber
import me.haliksar.securityalgorithms.libs.modexp.long.modExpRec
import kotlin.properties.Delegates

class RsaCipherLong :
    Encrypt<Long, Long, RsaCipherLong.Keys> {

    data class Keys(var publicKey: Pair<Long, Long>, var privateKey: Pair<Long, Long>)

    override var keys: Keys? = null
    override var keysData: Keys by Delegates.notNull()

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

    override fun validate() {}
}