package me.haliksar.securityalgorithms.libs.diffiehellmankeyexchange.long

import me.haliksar.securityalgorithms.libs.core.prime.isPrime
import me.haliksar.securityalgorithms.libs.core.prime.randomLong
import me.haliksar.securityalgorithms.libs.core.prime.randomPrimeNumber
import me.haliksar.securityalgorithms.libs.modexp.long.modExp

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
fun diffieHellman(p: Long, g: Long, xa: Long, xb: Long): Long {
    val q = (p - 1L) / 2L
    check(q.isPrime()) {
        "По алгоритму Ферма число q = $q должно быть простым!"
    }
    check(p.isPrime()) {
        "По алгоритму Ферма число p = $p должно быть простым!"
    }
    check(g in 1L until p) {
        "Нарушено условие 1 < g < p [g = $g, p = $p]"
    }
    check(g.modExp(q, p) != 1L) {
        "Нарушено условие g^q mod(p) != 1 - число g должно быть первообразной корня по модулю p [g = $g, q = $q, p = $p] "
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
    val sharedKey = diffieHellman(p, g, xa, xb)
    println(sharedKey)
}