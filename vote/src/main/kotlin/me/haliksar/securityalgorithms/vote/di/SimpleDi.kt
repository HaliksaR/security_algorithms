package me.haliksar.securityalgorithms.vote.di

import me.haliksar.securityalgorithms.vote.client.Client
import me.haliksar.securityalgorithms.vote.client.ClientImpl
import me.haliksar.securityalgorithms.vote.server.Server
import me.haliksar.securityalgorithms.vote.server.ServerImpl

object SimpleDi {

    fun getServer(question: String): Server = ServerImpl(question)

    fun getClient(id: Int, server: Server): Client = ClientImpl(id, server)
}