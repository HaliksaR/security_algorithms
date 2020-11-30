package me.haliksar.securityalgorithms.vote.client

import me.haliksar.securityalgorithms.libs.core.hashutils.md5L
import me.haliksar.securityalgorithms.libs.core.prime.mutuallyPrime
import me.haliksar.securityalgorithms.libs.gcd.extendedGcdTailRec
import me.haliksar.securityalgorithms.libs.modexp.modExpRec
import me.haliksar.securityalgorithms.vote.server.Server

interface Client {

    fun vote()
}

class ClientImpl(
    private val id: Int,
    private val server: Server,
) : Client {

    private companion object {
        val ANSWERS = listOf("Yes", "No", "I don't know")
    }

    override fun vote() {
        val (d, n) = server.publicKey
        val vote = ANSWERS.random()
        val message = "${server.question} -> $vote"
        val r = Long.mutuallyPrime(n)
        val hash = getHash(message, r, d, n)
        val bulletin = server.getBulletin(id, hash)
        var rInv = r.extendedGcdTailRec(n).y
        if (rInv < 1) rInv += n
        val s = (bulletin * rInv) % n
        server.sendBulletin(message, s)
    }

    private fun getHash(message: String, r: Long, d: Long, n: Long): Long {
        var hash = r.modExpRec(d, n, message.md5L % n)
        if (hash < 0) hash += n
        check(hash < n) { "The n value is too small! hash = $hash, n = $n" }
        return hash
    }
}