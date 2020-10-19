package me.haliksar.securityalgorithms.libs.ciphers.cipher

import me.haliksar.securityalgorithms.libs.ciphers.contract.ElectronicSignature
import me.haliksar.securityalgorithms.libs.core.hashutils.SHA_256L
import me.haliksar.securityalgorithms.libs.core.prime.*
import me.haliksar.securityalgorithms.libs.gcd.long.gcdTailRec
import me.haliksar.securityalgorithms.libs.modexp.long.modExpRec
import kotlin.properties.Delegates

class GostElectronicSignatureLong :
    ElectronicSignature<Byte, GostElectronicSignatureLong.HashData, GostElectronicSignatureLong.Keys> {

    data class HashData(val m: Byte, val r: Long, val s: Long)

    data class Keys(val p: Long, val q: Long, val a: Long, val x: Long, val y: Long)

    override var keys: Keys? = null
    private var keysData: Keys by Delegates.notNull()

    override fun generate() {
        val (p, q) = Long.pq
        val g = Long.randomNumber
        val a = g.modExpRec((p - 1) / q, p)
        val x = Long.antiderivative(q)
        val y = a.modExpRec(x, p)
        keysData = Keys(p, q, a, x, y)
        keys = keysData
    }

    override fun validate() {
        check(keysData.p.isPrime()) { "Число p = ${keysData.p} должно быть простым!" }
        check(keysData.q.isPrime()) { "Число q = ${keysData.q} должно быть простым!" }
        check(keysData.a.modExpRec(keysData.q, keysData.p) == 1L) { "Нарушено условие: a^q mod p = 1" }
    }

    override fun sign(message: Byte): HashData {
        val h = message.SHA_256L
        check(h in 1 until keysData.q) { "Сообщение должно быть меньше чем [message = $h, p = ${keysData.p}]" }
        var r: Long
        var s: Long
        do {
            val k: Long = Long.mutuallyPrime(keysData.p - 1)
            r = keysData.a.modExpRec(k, keysData.p) % keysData.q
            s = (k * h + keysData.x * r) % keysData.q
        } while (r == 0L || s == 0L)
        return HashData(message, r, s)
    }

    override fun verify(data: HashData): Boolean =
        with(data) {
            val h = m.SHA_256L
            check(r in 1 until keysData.q) { "0 < r = $r < q = ${keysData.q}" }
            check(s in 1 until keysData.q) { "0 < r = $s < q = ${keysData.q}" }
            val eucl = h gcdTailRec keysData.q
            var hInv = eucl.x
            if (hInv < 1) {
                hInv += keysData.p - 1
            }
            val u1 = (s * hInv) % keysData.q
            var u2 = (-r * hInv) % keysData.q
            if (u2 < 1) {
                u2 += keysData.q
            }
            val v1 = keysData.a.modExpRec(u1, keysData.p)
            val v2 = keysData.y.modExpRec(u2, keysData.p)
            val v = ((v1 * v2) % keysData.p) % keysData.q
            v == r
        }
}