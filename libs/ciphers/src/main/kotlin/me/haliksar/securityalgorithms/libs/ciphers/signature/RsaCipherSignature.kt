package me.haliksar.securityalgorithms.libs.ciphers.signature

import me.haliksar.securityalgorithms.libs.ciphers.contract.ElectronicSignature
import me.haliksar.securityalgorithms.libs.core.prime.multiplicativeInverse
import me.haliksar.securityalgorithms.libs.core.prime.mutuallyPrime
import me.haliksar.securityalgorithms.libs.core.prime.randomPrimeNumber
import me.haliksar.securityalgorithms.libs.modexp.modExpRec

class RsaCipherSignature :
    ElectronicSignature<Long, RsaCipherSignature.Keys, Long> {

    data class Keys(var publicKey: Pair<Long, Long>, var privateKey: Pair<Long, Long>)

    override fun generate(): Keys {
        val p = Long.randomPrimeNumber
        val q = Long.randomPrimeNumber
        val n = p * q // модуль
        val f = (p - 1L) * (q - 1L) // функция Эйлера
        val e = Long.mutuallyPrime(f) // открытая экспонента, простая из чисел Ферма
        val d = Long.multiplicativeInverse(e, f)  // Секретная экспонента
        return Keys(publicKey = e to n, privateKey = d to n)
    }

    override fun sign(message: Long, keys: Keys): Long {
        val (c, n) = keys.privateKey
        return message.modExpRec(c, n)
    }

    override fun verify(message: Long, signature: Long, keys: Keys): Boolean {
        val (e, n) = keys.publicKey
        return signature.modExpRec(e, n) == message
    }

    override fun validate(keys: Keys) {}
}