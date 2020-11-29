package me.haliksar.securityalgorithms.vote.processor

interface VoteProcessor {
    data class Keys(var publicKey: Pair<Long, Long>, var privateKey: Pair<Long, Long>)

    val keys: Keys

    fun vote(clientId: Long, voteHash: Long): Long
    fun verify(vote: Long, s: Long): Long
}