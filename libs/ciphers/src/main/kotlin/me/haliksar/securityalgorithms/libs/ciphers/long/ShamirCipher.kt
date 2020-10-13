package me.haliksar.securityalgorithms.libs.ciphers.long

import me.haliksar.securityalgorithms.libs.ciphers.Encrypt
import me.haliksar.securityalgorithms.libs.ciphers.EncryptWrapper
import me.haliksar.securityalgorithms.libs.core.fileutils.toFile
import me.haliksar.securityalgorithms.libs.core.fileutils.toLongList
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
@ExperimentalUnsignedTypes
class ShamirCipher : Encrypt<Long, Long> {

    private class PrimeSet(prime: Long) {
        val mutual: Long = Long.mutuallyPrime(prime - 1L)
        val multiInverse: Long = Long.multiplicativeInverse(mutual, prime - 1)
    }

    private var prime: Long = 0L
    private lateinit var setA: PrimeSet
    private lateinit var setB: PrimeSet

    override fun generate() {
        prime = Long.randomPrimeNumber
        setA = PrimeSet(prime)
        setB = PrimeSet(prime)
    }

    override fun validate() {
        check(setA.mutual.gcdTailRec(prime - 1L).nod == 1L) {
            "Число mutuallyPrime1=$setA.mutual должно быть взаимно простое с prime-1 = ${prime - 1L}"
        }
        check(((setA.mutual % (prime - 1L)) * (setA.multiInverse % (prime - 1L))) % (prime - 1L) == 1L) {
            "Нарушено условие '(d * e) mod(p - 1) = 1 [d = ${setA.multiInverse}, e = ${setA.mutual}, p = $prime]"
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

@ExperimentalUnsignedTypes
fun main() {
    val path = "libs/ciphers/src/main/resources"
    val image = "$path/image.jpg".toLongList()
    val method = ShamirCipher()
    val wrapper = EncryptWrapper(method)
    val encrypt = wrapper.encrypt(image)
    encrypt toFile "$path/encrypt.jpg"
    val decrypt = wrapper.decrypt(encrypt)
    decrypt toFile "$path/decrypt.jpg"
}