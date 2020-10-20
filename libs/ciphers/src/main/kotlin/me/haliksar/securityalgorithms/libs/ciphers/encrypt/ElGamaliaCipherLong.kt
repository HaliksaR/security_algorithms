package me.haliksar.securityalgorithms.libs.ciphers.encrypt

import me.haliksar.securityalgorithms.libs.ciphers.contract.Encrypt
import me.haliksar.securityalgorithms.libs.core.prime.antiderivative
import me.haliksar.securityalgorithms.libs.core.prime.isPrime
import me.haliksar.securityalgorithms.libs.core.prime.randomPrimeNumber
import me.haliksar.securityalgorithms.libs.modexp.long.modExpRec
import kotlin.properties.Delegates


// https://it.rfei.ru/course/~k017/~V8u3Fj4l/~hIGNMjZS
class ElGamaliaCipherLong :
    Encrypt<Long, ElGamaliaCipherLong.DataRet, ElGamaliaCipherLong.Keys> {

    data class Keys(var p: Long, var g: Long, var c: Long, var d: Long, var k: Long)

    data class DataRet(val r: Long, val e: Long)

    override var keys: Keys? = null
    override var keysData: Keys by Delegates.notNull()

    override fun generate() {
        val p = Long.randomPrimeNumber
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
        check(keysData.c in 1L until keysData.p) {
            "Нарушено условие '1 < x < p-1'! [x = ${keysData.c}, p = ${keysData.p}]"
        }
        check(keysData.k in 1L until keysData.p) {
            "Нарушено условие '1 < k < p-2'! [k = ${keysData.k}, p = ${keysData.p}]"
        }
        check(keysData.g.modExpRec(keysData.c, keysData.p) != 1L) {
            "Нарушено условие 'g ^ x mod p != 1' - число g должно быть первообразной корня по модулю p! [g = ${keysData.g}, x = ${keysData.c}, p = ${keysData.p}] "
        }
    }

    override fun encrypt(message: Long): DataRet {
        check(message < keysData.p) {
            "Сообщение должно быть меньше чем [message = $message, p = ${keysData.p}]"
        }
        val r = keysData.g.modExpRec(keysData.k, keysData.p)
        val e = keysData.d.modExpRec(keysData.k, keysData.p, message)
        return DataRet(r, e)
    }

    override fun decrypt(encryptData: DataRet): Long =
        encryptData.r.modExpRec(keysData.p - 1L - keysData.c, keysData.p, encryptData.e)
}