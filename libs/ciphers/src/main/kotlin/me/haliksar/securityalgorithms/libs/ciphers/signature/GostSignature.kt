package me.haliksar.securityalgorithms.libs.ciphers.signature

import me.haliksar.securityalgorithms.libs.ciphers.contract.ElectronicSignature
import me.haliksar.securityalgorithms.libs.core.prime.*
import me.haliksar.securityalgorithms.libs.gcd.gcdTailRec
import me.haliksar.securityalgorithms.libs.modexp.modExpRec
import java.io.Serializable

class GostSignature :
    ElectronicSignature<Long, GostSignature.Keys, GostSignature.Signature> {

    data class Signature(val r: Long, val s: Long) : Serializable
    data class Keys(val p: Long, val q: Long, val a: Long, val x: Long, val y: Long)

    override fun generate(): Keys {
        val (p, q) = Long.pq // р - большое простое число q - простой сомножитель числа (р -1)
        val a = Long.randomNumber.modExpRec((p - 1) / q,
            p) // а - любое число, меньшее (р-1), причем такое, что а^q mod p=1;
        val x = Long.antiderivative(q) // х - некоторое число, меньшее q;
        val y = a.modExpRec(x, p) // у = а^x mod р.
        return Keys(p = p, q = q, a = a, x = x, y = y)
    }

    override fun validate(keys: Keys) {
        check(keys.p.isPrime()) { "p = ${keys.p} must be prime!" }
        check(keys.q.isPrime()) { "q = ${keys.q} must be prime!" }
        check(keys.a.modExpRec(keys.q, keys.p) == 1L) { "Invalid: a^q mod p = 1" }
    }

    override fun sign(message: Long, keys: Keys): Signature {
        return with(keys) {
            check(message in 1 until q) { "The message must be less than\n [message = $message, p = ${p}]" }
            var r: Long
            var s: Long
            do {
                val k: Long =
                    Long.mutuallyPrime(keys.p - 1) // . Пользователь А генерирует случайное число k, причем k<q.
                r = a.modExpRec(k, p) % q // (а^k mod p) mod p,
                s = (x * r + k * message) % q // (х * r + k (Н(m))) mod p.
            } while (r == 0L || s == 0L) // Если Н(m) mod q=0, то значение Н(m) mod q принимают равным единице. Если r=0,
            // то выбирают другое значение k и начинают снова.
            Signature(r = r, s = s)
        }
    }

    override fun verify(message: Long, signature: Signature, keys: Keys): Boolean =
        with(signature) {
            with(keys) {
                // v = Н(m)^q-2 mod q
                val gcd = message gcdTailRec q
                var hInv = gcd.x
                if (hInv < 1) hInv += p - 1
                val sv = (s * hInv) % q
                var qrv = ((q - r) * hInv) % q
                if (qrv < 1) qrv += q
                // z1 = (s * v) mod q
                val az1 = a.modExpRec(sv, p)
                // z2 = ((q-r) * v) mod q
                val yz2 = y.modExpRec(qrv, p)
                val u = ((az1 * yz2) % p) % q
                u == r // Если u = r, то подпись считается верной.
            }
        }
}