package me.haliksar.securityalgorithms.libs.ciphers.signature

import me.haliksar.securityalgorithms.libs.ciphers.contract.ElectronicSignature
import me.haliksar.securityalgorithms.libs.core.prime.antiderivative
import me.haliksar.securityalgorithms.libs.core.prime.isPrime
import me.haliksar.securityalgorithms.libs.core.prime.mutuallyPrime
import me.haliksar.securityalgorithms.libs.core.prime.pq
import me.haliksar.securityalgorithms.libs.gcd.gcdTailRec
import me.haliksar.securityalgorithms.libs.modexp.modExpRec
import java.io.Serializable

class ElGamaliaSignature :
    ElectronicSignature<Long, ElGamaliaSignature.Keys, ElGamaliaSignature.Signature> {

    data class Signature(val a: Long, val b: Long, val y: Long, val p: Long, val g: Long) : Serializable
    data class Keys(var p: Long, var g: Long, var x: Long, var y: Long)

    override fun generate(): Keys {
        val (p, g) = Long.pq // большое простое целое число p // первообразный корень g
        val x = Long.antiderivative(p - 1) // Число Х является секретным ключом отправителя Отправитель А
        val y = g.modExpRec(x, p) // Число Y является открытым ключом Отправитель А
        return Keys(p, g, x, y)
    }

    override fun validate(keys: Keys) {
        check(keys.p.isPrime()) {
            "q = ${keys.p} must be prime!"
        }
        check(keys.x in 1L until keys.p) {
            "Invalid '1 < x < p-1'! [x = ${keys.x}, p = ${keys.p}]"
        }
        check(keys.g.modExpRec(keys.x, keys.p) != 1L) {
            "Invalid 'g ^ x mod p != 1' - g must be root antiderivative modulo p! [g = ${keys.g}, x = ${keys.x}, p = ${keys.p}] "
        }
    }

    override fun sign(message: Long, keys: Keys): Signature {
        return with(keys) {
            check(message in 1 until p) {
                "The message must be less than [message = $message, p = ${p}]"
            }

            val r = Long.mutuallyPrime(p - 1) // генерирует случайное целое число r
            check(r in 1L until p) {
                "Invalid '1 < k < p-1'! [k = ${r}, p = ${p}]"
            }
            val a = g.modExpRec(r, p) // а = g^r mod p

            // b = (H - ax) r^-1 (mod p - 1)
            val gcd = r gcdTailRec p - 1
            var kInv = gcd.x
            if (kInv < 1) kInv += p - 1
            var u = (message - x * a) % (p - 1)
            if (u < 1) u += p - 1
            val b = (kInv * u) % (p - 1)
            Signature(a = a, b = b, y = y, p = p, g = g)
        }
    }

    override fun verify(message: Long, signature: Signature, keys: Keys): Boolean =
        with(signature) {
            check(a in 0..p) {
                "Invalid '0 < a < p'"
            }
            check(b in 0 until p) {
                "Invalid '0 < b < p-1'"
            }
            // Y^a a^b (mod Р)
            // G^m (mod Р)
            (y.modExpRec(a, p) * a.modExpRec(b, p)) % p == g.modExpRec(message, p)
        }
}