package me.haliksar.securityalgorithms.libs.core.prime

import me.haliksar.securityalgorithms.libs.gcd.long.gcdTailRec
import kotlin.random.Random

internal const val MAX_RANDOM_RANGEL = 32000L //10 ^ 9
internal const val MIN_RANDOM_RANGEL = 1L

@ExperimentalUnsignedTypes
fun randomLong(limit: Long): Long = (1L until limit).random()

@ExperimentalUnsignedTypes
val Long.Companion.randomNumber: Long
    get() = Random.nextLong(MIN_RANDOM_RANGEL, MAX_RANDOM_RANGEL)

@ExperimentalUnsignedTypes
val Long.Companion.randomNumberCustom: (Long, Long) -> Long
    get() = { from, to -> Random.nextLong(from, to) }

@ExperimentalUnsignedTypes
val Long.Companion.randomPrimeNumber: Long
    get() {
        var x: Long
        do {
            x = randomNumber
        } while (!x.isPrime())
        return x
    }

/**
 * Получить взаимно простое число с [x]
 */
@ExperimentalUnsignedTypes
val Long.Companion.mutuallyPrime: (x: Long) -> Long
    get() = { x ->
        var e: Long
        do {
            e = randomNumberCustom(1, x)
        } while (e.gcdTailRec(x).nod != 1L)
        e
    }

/**
 * Инверсия числа
 * Получение числа мультипликативно обратное к числу [e] по модулю [p], такое что: d * e = 1 mod p
 */
@ExperimentalUnsignedTypes
val Long.Companion.multiplicativeInverse: (e: Long, p: Long) -> Long
    get() = { e, p ->
        var d: Long
        do {
            d = (1L + Random.nextLong() * p) / e
        } while (((e % p) * (d % p)) % p != 1L)
        while (d < 0L) {
            d += p
        }
        d
    }

@ExperimentalUnsignedTypes
fun Long.isPrime(): Boolean {
    if (this <= 1L) return false
    for (i in 2L until this) {
        if (this % i == 0L) {
            return false
        }
    }
    return true
}