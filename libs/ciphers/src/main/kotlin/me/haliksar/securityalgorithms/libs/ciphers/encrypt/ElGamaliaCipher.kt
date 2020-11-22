package me.haliksar.securityalgorithms.libs.ciphers.encrypt

import me.haliksar.securityalgorithms.libs.ciphers.contract.Encrypt
import me.haliksar.securityalgorithms.libs.core.prime.antiderivative
import me.haliksar.securityalgorithms.libs.core.prime.isPrime
import me.haliksar.securityalgorithms.libs.core.prime.mutuallyPrime
import me.haliksar.securityalgorithms.libs.core.prime.pq
import me.haliksar.securityalgorithms.libs.modexp.modExpRec


// https://it.rfei.ru/course/~k017/~V8u3Fj4l/~hIGNMjZS
class ElGamaliaCipher :
    Encrypt<Long, ElGamaliaCipher.DataRet, ElGamaliaCipher.Keys> {

    data class Keys(var p: Long, var g: Long, var c: Long, var d: Long)

    data class DataRet(val r: Long, val e: Long)

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
        check(keys.c in 1L until keys.p) {
            "Invalid '1 < x < p-1'! [x = ${keys.c}, p = ${keys.p}]"
        }
        check(keys.g.modExpRec(keys.c, keys.p) != 1L) {
            "Invalid 'g ^ x mod p != 1' - g must be the antiderivative of the root modulo p! [g = ${keys.g}, x = ${keys.c}, p = ${keys.p}] "
        }
    }

    override fun encrypt(message: Long, keys: Keys): DataRet {
        check(message < keys.p) {
            "The message must be less than [message = $message, p = ${keys.p}]"
        }
        val k = Long.mutuallyPrime(keys.p - 1)
        val r = keys.g.modExpRec(k, keys.p)
        val e = keys.d.modExpRec(k, keys.p, message)
        return DataRet(r, e)
    }

    override fun decrypt(encryptData: DataRet, keys: Keys): Long =
        encryptData.r.modExpRec(keys.p - 1L - keys.c, keys.p, encryptData.e)
}