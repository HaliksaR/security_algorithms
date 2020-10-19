package me.haliksar.securityalgorithms.libs.ciphers.encrypt

import me.haliksar.securityalgorithms.libs.ciphers.contract.Encrypt
import me.haliksar.securityalgorithms.libs.core.prime.antiderivative
import me.haliksar.securityalgorithms.libs.core.prime.isPrime
import me.haliksar.securityalgorithms.libs.core.prime.pq
import me.haliksar.securityalgorithms.libs.modexp.long.modExpRec
import kotlin.properties.Delegates

class ElGamaliaCipherLong :
    Encrypt<Long, ElGamaliaCipherLong.DataRet, ElGamaliaCipherLong.Keys> {

    data class HashData(val m: Byte, val r: Long, val s: Long, val y: Long, val p: Long, val g: Long)

    data class Keys(var p: Long, var g: Long, var x: Long, var y: Long, var k: Long)

    data class DataRet(val a: Long, val b: Long)

    override var keys: Keys? = null

    private var keysData: Keys by Delegates.notNull()

    override fun generate() {
        val p = Long.pq.first
        val x = Long.antiderivative(p)
        val g = Long.antiderivative(p)
        val k = Long.antiderivative(p)
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
            "Нарушено условие '1 < k < p-1'! [k = ${keysData.k}, p = ${keysData.p}]"
        }
        check(keysData.g.modExpRec(keysData.x, keysData.p) != 1L) {
            "Нарушено условие 'g ^ x mod p != 1' - число g должно быть первообразной корня по модулю p! [g = ${keysData.g}, x = ${keysData.x}, p = ${keysData.p}] "
        }
    }

    override fun encrypt(message: Long): DataRet {
        check(message < keysData.p) {
            "Сообщение должно быть меньше чем [message = $message, p = ${keysData.p}]"
        }
        val a = keysData.g.modExpRec(keysData.k, keysData.p)
        val b = keysData.y.modExpRec(keysData.k, keysData.p, message)
        return DataRet(a, b)
    }

    override fun decrypt(encryptData: DataRet): Long =
        encryptData.a.modExpRec(keysData.p - 1L - keysData.x, keysData.p, encryptData.b)
}