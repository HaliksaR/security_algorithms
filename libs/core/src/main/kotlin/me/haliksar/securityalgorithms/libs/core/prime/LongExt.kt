package me.haliksar.securityalgorithms.libs.core.prime

import me.haliksar.securityalgorithms.libs.gcd.long.gcdTailRec
import kotlin.random.Random

internal const val MAX_RANDOM_RANGEL = 32000L //10 ^ 9
internal const val MIN_RANDOM_RANGEL = 1L

fun randomLong(limit: Long): Long = (1L until limit).random()

val Long.Companion.randomNumber: Long
    get() = Random.nextLong(MIN_RANDOM_RANGEL, MAX_RANDOM_RANGEL)

val Long.Companion.randomNumberCustom: (Long, Long) -> Long
    get() = { from, to -> Random.nextLong(from, to) }

val Long.Companion.randomPrimeNumber: Long
    get() {
        var x: Long
        do {
            x = randomNumber
        } while (!x.isPrime())
        return x
    }

val Long.Companion.shortPrimeNumber: Long
    get() {
        var x: Long
        do {
            x = Random.nextLong(32500, 45000)
        } while (!x.isPrime())
        return x
    }

/**
 * Получить взаимно простое число с [x]
 */
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

fun Long.isPrime(): Boolean {
    if (this <= 1L) return false
    for (i in 2L until this) {
        if (this % i == 0L) {
            return false
        }
    }
    return true
}

val Long.Companion.pq: Pair<Long, Long>
    get() {
        var p: Long
        var q: Long
        do {
            p = Long.randomPrimeNumber
            q = (p - 1) / 2
        } while (!q.isPrime())
        return Pair(p, q)
    }

val Long.Companion.antiderivative: (Long) -> Long
    get() = {
        Random.nextLong(1L, it - 1L)
    }