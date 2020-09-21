package me.haliksar.securityalgorithms.libs.diffiehellmankeyexchange

import me.haliksar.securityalgorithms.libs.core.prime.isPrime
import me.haliksar.securityalgorithms.libs.core.prime.randomPrimeNumber
import me.haliksar.securityalgorithms.libs.core.prime.randomULong
import me.haliksar.securityalgorithms.libs.modexp.modExp

@ExperimentalUnsignedTypes
fun getRandomPQ(): Pair<ULong, ULong> {
    var p: ULong
    var q: ULong
    do {
        p = ULong.randomPrimeNumber
        q = (p - 1uL) / 2uL
    } while (!q.isPrime())
    return Pair(p, q)
}

/**
 * Функция построения общего ключа для двух абонентов по схеме Диффи-Хеллмана
 *
 * @param p [ULong] простое натуральное число
 * @param g [ULong] простое число
 * @param xa [ULong] закрытый ключ абонента A
 * @param xb [ULong] закрытый ключ абонента B
 *
 * @return секретный общий ключ
 **/
@ExperimentalUnsignedTypes
fun diffieHellman(p: ULong, g: ULong, xa: ULong, xb: ULong): ULong {
    val q = (p - 1uL) / 2uL
    check(q.isPrime()) {
        "По алгоритму Ферма число q = $q должно быть простым!"
    }
    check(p.isPrime()) {
        "По алгоритму Ферма число p = $p должно быть простым!"
    }
    check(g in 1uL..p - 1uL) {
        "Нарушено условие 1 < g < p [g = $g, p = $p]"
    }
    check(g.modExp(q, p) != 1uL) {
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
    val g = randomULong(p - 1uL)
    val xa = randomULong(p)
    val xb = randomULong(p)
    val sharedKey = diffieHellman(p, g, xa, xb)
    println(sharedKey)
}