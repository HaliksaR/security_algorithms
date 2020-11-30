package me.haliksar.securityalgorithms.vote

import me.haliksar.securityalgorithms.vote.client.Client
import me.haliksar.securityalgorithms.vote.di.SimpleDi
import me.haliksar.securityalgorithms.vote.server.Server


class VotingWindow(
    countClient: Int,
    question: String,
) {

    private val server: Server = SimpleDi.getServer(question)
    private val clients = mutableListOf<Client>()

    init {
        for (id in 1..countClient) {
            clients.add(SimpleDi.getClient(id, server))
        }
    }

    fun clientsVote(): List<String> {
        clients.forEach { it.vote() }
        return server.result
    }

    fun printClientCount(): VotingWindow {
        println("Clients: ${clients.size}\n")
        return this
    }
}

fun List<String>.groupByMessage(): Map<String, Int> =
    groupingBy { it }.eachCount()

fun Map<String, Int>.printResults(): Unit =
    forEach { (key, value) -> println("$key: $value") }

fun main() = VotingWindow(
    countClient = (1..300).random(),
    question = "Test"
).printClientCount()
    .clientsVote()
    .groupByMessage()
    .printResults()