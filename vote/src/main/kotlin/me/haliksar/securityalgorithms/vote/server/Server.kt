package me.haliksar.securityalgorithms.vote.server

interface Server {
    fun showVotes()
    fun vote(clientId: Long, voteHash: Long)
    fun verify(vote: Long, s: Long)
}