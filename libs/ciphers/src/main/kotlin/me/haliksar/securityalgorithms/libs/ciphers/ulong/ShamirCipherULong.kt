package me.haliksar.securityalgorithms.libs.ciphers.ulong

import me.haliksar.securityalgorithms.libs.ciphers.Encrypt
import me.haliksar.securityalgorithms.libs.core.prime.multiplicativeInverse
import me.haliksar.securityalgorithms.libs.core.prime.mutuallyPrime
import me.haliksar.securityalgorithms.libs.core.prime.randomPrimeNumber
import me.haliksar.securityalgorithms.libs.gcd.ulong.gcdTailRec
import me.haliksar.securityalgorithms.libs.modexp.ulong.modExpRec


/**
 * Шифр Шамира
 *
 * [prime] заведомо большое простое число
 * [setA] числа, которые выбрал абонент A
 * [setB] числа, которые выбрал абонент B
 */
@ExperimentalUnsignedTypes
class ShamirCipherULong : Encrypt<ULong, ULong> {

    private class PrimeSet(prime: ULong) {
        val mutual: ULong = ULong.mutuallyPrime(prime - 1uL)
        val multiInverse: ULong = ULong.multiplicativeInverse(mutual, prime - 1uL)
    }

    private var prime: ULong = 0uL
    private lateinit var setA: PrimeSet
    private lateinit var setB: PrimeSet

    override fun generate() {
        prime = ULong.randomPrimeNumber
        setA = PrimeSet(prime)
        setB = PrimeSet(prime)
    }

    override fun validate() {
        check(setA.mutual.gcdTailRec(prime - 1uL).nod == 1uL) {
            "Число mutuallyPrime1=$setA.mutual должно быть взаимно простое с prime-1 = ${prime - 1uL}"
        }
        check(((setA.mutual % (prime - 1uL)) * (setA.multiInverse % (prime - 1uL))) % (prime - 1uL) == 1uL) {
            "Нарушено условие '(d * e) mod(p - 1) = 1 [d = ${setA.multiInverse}, e = ${setA.mutual}, p = $prime]"
        }
    }

    override fun encrypt(message: ULong): ULong {
        val value = message.modExpRec(setA.mutual, prime)
        return value.modExpRec(setB.multiInverse, prime)
    }

    override fun decrypt(encryptData: ULong): ULong {
        val value = encryptData.modExpRec(setA.multiInverse, prime)
        return value.modExpRec(setB.mutual, prime)
    }
}