package me.haliksar.securityalgorithms.libs.ciphers.encrypt

import me.haliksar.securityalgorithms.libs.ciphers.contract.Encrypt
import me.haliksar.securityalgorithms.libs.core.prime.multiplicativeInverse
import me.haliksar.securityalgorithms.libs.core.prime.mutuallyPrime
import me.haliksar.securityalgorithms.libs.core.prime.randomPrimeNumber
import me.haliksar.securityalgorithms.libs.gcd.long.gcdTailRec
import me.haliksar.securityalgorithms.libs.modexp.long.modExpRec
import kotlin.properties.Delegates


/**
 * Шифр Шамира
 * https://studme.org/239582/informatika/shifr_shamira#:~:text=%D0%A8%D0%B8%D1%84%D1%80%20%D0%A8%D0%B0%D0%BC%D0%B8%D1%80%D0%B0%20(Adi%20Shamir)%20%D0%B1%D1%8B%D0%BB,%D1%81%D0%BE%D0%BE%D0%B1%D1%89%D0%B5%D0%BD%D0%B8%D1%8F%20%D0%BF%D0%BE%D1%82%D1%80%D0%B5%D0%B1%D1%83%D0%B5%D1%82%20%D0%B8%D1%81%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D1%8F%20%D0%BD%D0%B5%D0%BA%D0%BE%D1%82%D0%BE%D1%80%D0%BE%D0%B3%D0%BE%20%D1%88%D0%B8%D1%84%D1%80%D0%B0.
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
    override var keysData: Keys by Delegates.notNull()

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
            "Число mutual1 = ${keysData.a.mutual} должно быть взаимно простое с p - 1 = ${keysData.prime - 1L}"
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