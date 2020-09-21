package me.haliksar.securityalgorithms.libs.core.prime

import kotlin.random.Random

internal const val MAX_RANDOM_RANGE = 32000 //10 ^ 9
internal const val MIN_RANDOM_RANGE = 1

@ExperimentalUnsignedTypes
fun randomULong(limit: ULong): ULong = (1uL until limit).random()

@ExperimentalUnsignedTypes
val ULong.Companion.randomNumber: ULong
    get() = Random.nextInt(MIN_RANDOM_RANGE, MAX_RANDOM_RANGE).toULong()

@ExperimentalUnsignedTypes
val ULong.Companion.randomPrimeNumber: ULong
    get() {
        var x: ULong
        do {
            x = randomNumber
        } while (!x.isPrime())
        return x
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