@file:Suppress("EXPERIMENTAL_UNSIGNED_LITERALS")

package me.haliksar.securityalgorithms.libs.core.prime

import me.haliksar.securityalgorithms.libs.gcd.ulong.gcdTailRec
import kotlin.random.Random
import kotlin.random.nextULong

internal const val MAX_RANDOM_RANGEUL = 32000uL //10 ^ 9
internal const val MIN_RANDOM_RANGEUL = 1uL

@ExperimentalUnsignedTypes
fun randomULong(limit: ULong): ULong = (1uL until limit).random()

@ExperimentalUnsignedTypes
val ULong.Companion.randomNumber: ULong
    get() = Random.nextULong(MIN_RANDOM_RANGEUL, MAX_RANDOM_RANGEUL)

@ExperimentalUnsignedTypes
val ULong.Companion.randomNumberCustom: (ULong, ULong) -> ULong
    get() = { from, to -> Random.nextULong(from, to) }

@ExperimentalUnsignedTypes
val ULong.Companion.randomPrimeNumber: ULong
    get() {
        var x: ULong
        do {
            x = randomNumber
        } while (!x.isPrime())
        return x
    }

/**
 * Получить взаимно простое число с [x]
 */
@ExperimentalUnsignedTypes
val ULong.Companion.mutuallyPrime: (x: ULong) -> ULong
    get() = { x ->
        var e: ULong
        do {
            e = randomNumberCustom(1u, x)
        } while (e.gcdTailRec(x).nod != 1uL)
        e
    }

/**
 * Инверсия числа
 * Получение числа мультипликативно обратное к числу [e] по модулю [p], такое что: d * e = 1 mod p
 */
@ExperimentalUnsignedTypes
val ULong.Companion.multiplicativeInverse: (e: ULong, p: ULong) -> ULong
    get() = { e, p ->
        var d: ULong
        do {
            d = (1uL + Random.nextULong() * p) / e
        } while (((e % p) * (d % p)) % p != 1uL)
        while (d < 0uL) {
            d += p
        }
        d
    }

@ExperimentalUnsignedTypes
fun ULong.isPrime(): Boolean {
    if (this <= 1uL) return false
    for (i in 2uL until this) {
        if (this % i == 0uL) {
            return false
        }
    }
    return true
}