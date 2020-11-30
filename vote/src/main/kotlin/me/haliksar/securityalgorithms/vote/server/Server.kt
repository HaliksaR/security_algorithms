package me.haliksar.securityalgorithms.vote.server

import me.haliksar.securityalgorithms.libs.core.hashutils.md5L
import me.haliksar.securityalgorithms.libs.core.prime.mutuallyPrime
import me.haliksar.securityalgorithms.libs.core.prime.randomPrimeNumber
import me.haliksar.securityalgorithms.libs.gcd.extendedGcdTailRec
import me.haliksar.securityalgorithms.libs.modexp.modExpRec

interface Server {

    val publicKey: Pair<Long, Long>

    val question: String

    val result: List<String>

    fun getBulletin(id: Int, hash: Long): Long

    fun sendBulletin(message: String, s: Long)
}

class ServerImpl(
    override val question: String,
) : Server {

    private val _result = mutableListOf<String>()
    override val result: List<String>
        get() = _result.toList()

    private val voting = mutableListOf<Int>()
    override lateinit var publicKey: Pair<Long, Long>
    private lateinit var privateKey: Pair<Long, Long>

    init {
        rsa()
    }

    private fun rsa() {
        val p = Long.randomPrimeNumber
        val q = Long.randomPrimeNumber
        val n = p * q // модуль
        val f = (p - 1L) * (q - 1L) // функция Эйлера
        val d = Long.mutuallyPrime(f) // открытая экспонента, простая из чисел Ферма
        var c = d.extendedGcdTailRec(f).y  // Секретная экспонента
        if (c < 0) c += f
        publicKey = Pair(d, n)
        privateKey = Pair(c, n)
    }

    override fun getBulletin(id: Int, hash: Long): Long {
        check(!voting.contains(id)) { "This user has already issued a Bulletin!" }
        voting.add(id)
        val (c, N) = privateKey
        return hash.modExpRec(c, N)
    }

    override fun sendBulletin(message: String, s: Long) {
        validateHash(message, s)
        _result.add(message)
    }

    private fun validateHash(message: String, s: Long) {
        val (d, n) = publicKey
        var hash = message.md5L % n
        if (hash < 0) hash += n
        check(hash == s.modExpRec(d, n)) { "The Bulletin has not been tested for correctness!" }
    }
}