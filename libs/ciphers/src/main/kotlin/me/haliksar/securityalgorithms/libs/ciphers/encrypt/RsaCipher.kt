package me.haliksar.securityalgorithms.libs.ciphers.encrypt

import me.haliksar.securityalgorithms.libs.ciphers.contract.Encrypt
import me.haliksar.securityalgorithms.libs.core.prime.multiplicativeInverse
import me.haliksar.securityalgorithms.libs.core.prime.mutuallyPrime
import me.haliksar.securityalgorithms.libs.core.prime.randomPrimeNumber
import me.haliksar.securityalgorithms.libs.modexp.modExpRec

// http://www.michurin.net/computer-science/rsa.html
class RsaCipher :
    Encrypt<Long, Long, RsaCipher.Keys> {

    data class Keys(var publicKey: Pair<Long, Long>, var privateKey: Pair<Long, Long>)

    override fun generate(): Keys {
        val p = Long.randomPrimeNumber
        val q = Long.randomPrimeNumber
        val n = p * q // модуль
        val f = (p - 1L) * (q - 1L) // функция Эйлера
        val e = Long.mutuallyPrime(f) // открытая экспонента, простая из чисел Ферма
        val d = Long.multiplicativeInverse(e, f)  // Секретная экспонента
        return Keys(publicKey = Pair(e, n), privateKey = Pair(d, n))
    }

    override fun encrypt(message: Long, keys: Keys): Long {
        val (e, n) = keys.publicKey
        return message.modExpRec(e, n)
    }

    override fun decrypt(encryptData: Long, keys: Keys): Long {
        val (d, n) = keys.privateKey
        return encryptData.modExpRec(d, n)
    }

    override fun validate(keys: Keys) {}
}