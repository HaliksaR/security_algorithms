package me.haliksar.securityalgorithms.libs.babystepgiantstep

import me.haliksar.securityalgorithms.libs.core.prime.randomPrimeNumber
import me.haliksar.securityalgorithms.libs.modexp.modExp
import kotlin.math.sqrt

/**
 * Алгоритм Гельфонда — Шенкса (Шаг младенца - шаг великана)
 * @return степень x числа [a] по модулю [p] равное числу [y]
 */
fun babyStepGiantStep(a: Long, p: Long, y: Long, m: Long): Long {
    check(y < p) { "Invalid 'y < p'! [y = $y, p = $p]" }
    check(m * m > p) { "Invalid 'm * k > p'! [m = $m, p = $p]" }
    val map = mutableMapOf<Long, Long>()
    (0L until m).forEach { top ->
        map[a.modExp(top, p, y)] = top
    }
    (1L..m).forEach { top ->
        map[a.modExp(top * m, p)]?.let {
            val x = top * m - it
            check(a.modExp(x, p) == y) { "Неверное решение!" }
            return x
        }
    }
    throw IllegalStateException("Число X не найденно!")
}


@ExperimentalUnsignedTypes
fun main() {
    val a = Long.randomPrimeNumber
    val x1 = Long.randomPrimeNumber
    val p = Long.randomPrimeNumber
    val y = a.modExp(x1, p)
    val m = sqrt((p).toDouble()) + 1L
    println(babyStepGiantStep(a, p, y, m.toLong()))
}
