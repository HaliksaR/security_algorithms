package me.haliksar.securityalgorithms.libs.ciphers.signature

import me.haliksar.securityalgorithms.libs.ciphers.contract.ElectronicSignature
import me.haliksar.securityalgorithms.libs.core.hashutils.SHA_256L
import me.haliksar.securityalgorithms.libs.core.prime.antiderivative
import me.haliksar.securityalgorithms.libs.core.prime.isPrime
import me.haliksar.securityalgorithms.libs.core.prime.mutuallyPrime
import me.haliksar.securityalgorithms.libs.core.prime.pq
import me.haliksar.securityalgorithms.libs.gcd.long.gcdTailRec
import me.haliksar.securityalgorithms.libs.modexp.long.modExpRec

class ElGamaliaSignatureLong :
    ElectronicSignature<Byte, ElGamaliaSignatureLong.HashData, ElGamaliaSignatureLong.Keys> {

    data class HashData(val m: Byte, val r: Long, val s: Long, val y: Long, val p: Long, val g: Long)

    data class Keys(var p: Long, var g: Long, var x: Long, var y: Long, var k: Long)

    override var keys: Keys? = null

    override lateinit var keysData: Keys

    override fun generate() {
        val p = Long.pq.first
        val x = Long.antiderivative(p - 1)
        val g = Long.antiderivative(p - 1)
        val k = Long.antiderivative(p - 2)
        val y = g.modExpRec(x, p)
        keysData = Keys(p, g, x, y, k)
        keys = keysData
    }

    override fun validate() {
        check(keysData.p.isPrime()) {
            "По алгоритму Ферма число q = ${keysData.p} должно быть простым!"
        }
        check(keysData.x in 1L until keysData.p) {
            "Нарушено условие '1 < x < p-1'! [x = ${keysData.x}, p = ${keysData.p}]"
        }
        check(keysData.k in 1L until keysData.p) {
            "Нарушено условие '1 < k < p-2'! [k = ${keysData.k}, p = ${keysData.p}]"
        }
        check(keysData.g.modExpRec(keysData.x, keysData.p) != 1L) {
            "Нарушено условие 'g ^ x mod p != 1' - число g должно быть первообразной корня по модулю p! [g = ${keysData.g}, x = ${keysData.x}, p = ${keysData.p}] "
        }
    }

    override fun sign(message: Byte): HashData {
        val hash = message.SHA_256L
        check(hash in 1 until keysData.p) {
            "Сообщение должно быть меньше чем [message = $hash, p = ${keysData.p}]"
        }
        keysData.k = Long.mutuallyPrime(keysData.p - 1)
        val data = keysData.k gcdTailRec keysData.p - 1
        var kInv = data.x
        if (kInv < 1) {
            kInv += keysData.p - 1
        }
        check((keysData.k * kInv) % (keysData.p - 1L) == 1L) { "Нарушено условие" }
        val r = keysData.g.modExpRec(keysData.k, keysData.p)
        var u = (hash - keysData.x * r) % (keysData.p - 1)
        if (u < 1) {
            u += keysData.p - 1
        }
        val s = (kInv * u) % (keysData.p - 1)
        return HashData(message, r, s, keysData.y, keysData.p, keysData.g)
    }

    override fun verify(data: HashData): Boolean =
        with(data) {
            val hash = m.SHA_256L
            val x1 = y.modExpRec(r, p)
            val x2 = r.modExpRec(s, p)
            val x3 = (x1 * x2) % p
            val x4 = g.modExpRec(hash, p)
            x3 == x4
        }
}