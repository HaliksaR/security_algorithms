package me.haliksar.securityalgorithms.libs.ciphers.long

import me.haliksar.securityalgorithms.libs.ciphers.Encrypt
import me.haliksar.securityalgorithms.libs.core.prime.multiplicativeInverse
import me.haliksar.securityalgorithms.libs.core.prime.mutuallyPrime
import me.haliksar.securityalgorithms.libs.core.prime.randomPrimeNumber
import me.haliksar.securityalgorithms.libs.gcd.long.gcdTailRec
import me.haliksar.securityalgorithms.libs.modexp.long.modExpRec


/**
 * Шифр Шамира
 *
 * [prime] заведомо большое простое число
 * [setA] числа, которые выбрал абонент A
 * [setB] числа, которые выбрал абонент B
 */

class ShamirCipherLong : Encrypt<Long, Long, ShamirCipherLong.Keys> {

    data class Keys(
        val setA: PrimeSet,
        val setB: PrimeSet,
    ) {
        override fun toString() = "$setA\n$setB"
    }

    class PrimeSet(
        private val name: String,
        private val prime: Long
    ) {
        val mutual: Long = Long.mutuallyPrime(prime - 1L)
        val multiInverse: Long = Long.multiplicativeInverse(mutual, prime - 1)

        override fun toString() =
            "$name prime = $prime\n${name}1 = $mutual\n${name}2 = $multiInverse"

        fun printKeys() = println(this)
    }

    private var prime: Long = 0L
    private lateinit var setA: PrimeSet
    private lateinit var setB: PrimeSet

    override fun generate(): Keys {
        prime = Long.randomPrimeNumber
        setA = PrimeSet("A", prime)
        setB = PrimeSet("B", prime)
        return Keys(setA, setB)
    }

    override fun validate() {
        check(setA.mutual.gcdTailRec(prime - 1L).nod == 1L) {
            "Число mutuallyPrime1=$setA.mutual должно быть взаимно простое с p - 1 = ${prime - 1L}"
        }
        val mod = prime - 1L
        val modMutual = setA.mutual % mod
        val modMultiInverse = setA.multiInverse % mod
        check((modMultiInverse * modMutual) % mod == 1L) {
            "Нарушено условие '(y1 * y2) mod(p - 1) = 1 [y1 = ${setA.multiInverse}, y2 = ${setA.mutual}, p = $prime]"
        }
    }

    override fun encrypt(message: Long): Long {
        val value = message.modExpRec(setA.mutual, prime)
        return value.modExpRec(setB.multiInverse, prime)
    }

    override fun decrypt(encryptData: Long): Long {
        val value = encryptData.modExpRec(setA.multiInverse, prime)
        return value.modExpRec(setB.mutual, prime)
    }
}