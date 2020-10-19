package me.haliksar.securityalgorithms.libs.ciphers.cipher

import me.haliksar.securityalgorithms.libs.ciphers.contract.Encrypt
import me.haliksar.securityalgorithms.libs.core.prime.multiplicativeInverse
import me.haliksar.securityalgorithms.libs.core.prime.mutuallyPrime
import me.haliksar.securityalgorithms.libs.core.prime.randomPrimeNumber
import me.haliksar.securityalgorithms.libs.gcd.long.gcdTailRec
import me.haliksar.securityalgorithms.libs.modexp.long.modExpRec
import kotlin.properties.Delegates


/**
 * Шифр Шамира
 *
 * [prime] заведомо большое простое число
 * [keysData.a] числа, которые выбрал абонент A
 * [keysData.b] числа, которые выбрал абонент B
 */

class ShamirCipherLong : Encrypt<Long, Long, ShamirCipherLong.Keys> {

    data class Keys(
        val prime: Long,
        val a: PrimeSet,
        val b: PrimeSet,
    ) {
        override fun toString() = "$a\n$b"
    }

    override var keys: Keys? = null
    private var keysData: Keys by Delegates.notNull()

    class PrimeSet(
        private val name: String,
        private val prime: Long
    ) {
        val mutual: Long = Long.mutuallyPrime(prime - 1L)
        val multiInverse: Long = Long.multiplicativeInverse(mutual, prime - 1)

        override fun toString() =
            "$name prime = $prime\n${name}1 = $mutual\n${name}2 = $multiInverse"
    }

    override fun generate() {
        val prime = Long.randomPrimeNumber
        val a = PrimeSet("A", prime)
        val b = PrimeSet("B", prime)
        keysData = Keys(prime, a, b)
        keys = keysData
    }

    override fun validate() {
        check(keysData.a.mutual.gcdTailRec(keysData.prime - 1L).nod == 1L) {
            "Число mutuallyPrime1=$keysData.a.mutual должно быть взаимно простое с p - 1 = ${keysData.prime - 1L}"
        }
        val mod = keysData.prime - 1L
        val modMutual = keysData.a.mutual % mod
        val modMultiInverse = keysData.a.multiInverse % mod
        check((modMultiInverse * modMutual) % mod == 1L) {
            "Нарушено условие '(y1 * y2) mod(p - 1) = 1 [y1 = ${keysData.a.multiInverse}, y2 = ${keysData.a.mutual}, p = ${keysData.prime}]"
        }
    }

    override fun encrypt(message: Long): Long {
        val value = message.modExpRec(keysData.a.mutual, keysData.prime)
        return value.modExpRec(keysData.b.multiInverse, keysData.prime)
    }

    override fun decrypt(encryptData: Long): Long {
        val value = encryptData.modExpRec(keysData.a.multiInverse, keysData.prime)
        return value.modExpRec(keysData.b.mutual, keysData.prime)
    }
}