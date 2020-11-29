package me.haliksar.securityalgorithms.vote.processor

import me.haliksar.securityalgorithms.libs.core.hashutils.SHA_256L
import me.haliksar.securityalgorithms.libs.core.prime.multiplicativeInverse
import me.haliksar.securityalgorithms.libs.core.prime.mutuallyPrime
import me.haliksar.securityalgorithms.libs.core.prime.randomPrimeNumber
import me.haliksar.securityalgorithms.libs.modexp.modExpRec

class VoteProcessorImpl(
    private val voting: MutableList<Long> = mutableListOf(),
) : VoteProcessor {

    override val keys: VoteProcessor.Keys = generate()

    private fun generate(): VoteProcessor.Keys {
        val p = Long.randomPrimeNumber
        val q = Long.randomPrimeNumber
        val n = p * q // модуль
        val f = (p - 1L) * (q - 1L) // функция Эйлера
        val e = Long.mutuallyPrime(f) // открытая экспонента, простая из чисел Ферма
        val d = Long.multiplicativeInverse(e, f)  // Секретная экспонента
        return VoteProcessor.Keys(publicKey = Pair(e, n), privateKey = Pair(d, n))
    }

    override fun vote(clientId: Long, voteHash: Long): Long {
        check(!voting.contains(clientId)) { "Этому пользователю уже выдавали биллютень!" }
        voting.add(clientId)
        val (e, n) = keys.publicKey
        return voteHash.modExpRec(e, n)
    }

    override fun verify(vote: Long, s: Long): Long {
        val (d, n) = keys.privateKey
        val hash = vote.toByte().SHA_256L
        val verify = s.modExpRec(d, n)
        check(hash == verify) { "Билютеть не прошла проверку на корректность!" }
        return n % 10
    }
}