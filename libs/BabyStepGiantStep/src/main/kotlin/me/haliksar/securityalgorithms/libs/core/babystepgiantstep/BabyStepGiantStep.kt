package me.haliksar.securityalgorithms.libs.core.babystepgiantstep

import me.haliksar.securityalgorithms.libs.core.prime.randomPrimeNumber
import me.haliksar.securityalgorithms.libs.modexp.ulong.modExp
import kotlin.math.sqrt

/**
 * Алгоритм Гельфонда — Шенкса (Шаг младенца - шаг великана)
 * @return степень x числа [a] по модулю [p] равное числу [y]
 */
@ExperimentalUnsignedTypes
fun babyStepGiantStep(a: ULong, p: ULong, y: ULong, m: ULong): ULong {
    check(y < p) { "Нарушено условие 'y < p'! [y = $y, p = $p]" }
    check(m * m > p) { "Нарушено условие 'm * k > p'! [m = $m, p = $p]" }
    val map = mutableMapOf<ULong, ULong>()
    (0uL..(m - 1uL)).forEach { top ->
        map[a.modExp(top, p, y)] = top
    }
    (1uL..m).forEach { top ->
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
    val a = ULong.randomPrimeNumber
    val x1 = ULong.randomPrimeNumber
    val p = ULong.randomPrimeNumber
    val y = a.modExp(x1, p)
    val m = sqrt((p).toDouble()).toULong() + 1uL
    println(babyStepGiantStep(a, p, y, m))
}
