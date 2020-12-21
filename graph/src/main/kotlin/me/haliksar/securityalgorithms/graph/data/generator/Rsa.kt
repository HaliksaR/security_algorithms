package me.haliksar.securityalgorithms.graph.data.generator

import me.haliksar.securityalgorithms.libs.core.prime.mutuallyPrime
import me.haliksar.securityalgorithms.libs.core.prime.randomPrimeNumber
import me.haliksar.securityalgorithms.libs.gcd.extendedGcdTailRec

data class Rsa(
    val publicKey: Pair<Long, Long>,
    val privateKey: Pair<Long, Long>,
) {

    companion object {

        fun generate(): Rsa {
            val p = Long.randomPrimeNumber
            val q = Long.randomPrimeNumber
            val n = p * q // модуль
            val f = (p - 1L) * (q - 1L) // функция Эйлера
            val d = Long.mutuallyPrime(f) // открытая экспонента, простая из чисел Ферма
            var c = d.extendedGcdTailRec(f).y  // Секретная экспонента
            if (c < 0) c += f
            return Rsa(publicKey = Pair(d, n), privateKey = Pair(c, n))
        }
    }
}