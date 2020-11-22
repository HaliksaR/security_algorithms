package me.haliksar.securityalgorithms.libs.diffiehellmankeyexchange

import me.haliksar.securityalgorithms.libs.core.prime.isPrime
import me.haliksar.securityalgorithms.libs.core.prime.randomLong
import me.haliksar.securityalgorithms.libs.core.prime.randomPrimeNumber
import me.haliksar.securityalgorithms.libs.modexp.modExp

@ExperimentalUnsignedTypes
fun getRandomPQ(): Pair<Long, Long> {
    var p: Long
    var q: Long
    do {
        p = Long.randomPrimeNumber
        q = (p - 1L) / 2L
    } while (!q.isPrime())
    return Pair(p, q)
}

/**
 * Функция построения общего ключа для двух абонентов по схеме Диффи-Хеллмана
 *
 * @param p [Long] простое натуральное число
 * @param g [Long] простое число
 * @param xa [Long] закрытый ключ абонента A
 * @param xb [Long] закрытый ключ абонента B
 *
 * @return секретный общий ключ
 **/
@ExperimentalUnsignedTypes
fun diffieHellman(p: Long, q: Long, g: Long, xa: Long, xb: Long): Long {
    check(q.isPrime()) {
        "q = $q must be prime!"
    }
    check(p.isPrime()) {
        "p = $p must be prime!"
    }
    check(g in 1L until p) {
        "Invalid 1 < g < p [g = $g, p = $p]"
    }
    check(g.modExp(q, p) != 1L) {
        "Invalid g^q mod(p) != 1 - g must be the antiderivative of the root modulo p [g = $g, q = $q, p = $p] "
    }
    val ya = g.modExp(xa, p)
    val yb = g.modExp(xb, p)
    val zab = yb.modExp(xa, p)
    val zba = ya.modExp(xb, p)
    check(zab == zba) {
        "Ключи не совпадают! ключ1 = $zab , ключ2 = $zba"
    }
    return zab
}


@ExperimentalUnsignedTypes
fun main(args: Array<String>) {
    val (p, q) = getRandomPQ()
    val g = randomLong(p - 1L)
    val xa = randomLong(p)
    val xb = randomLong(p)
    val sharedKey = diffieHellman(p, q, g, xa, xb)
    println(sharedKey)
}