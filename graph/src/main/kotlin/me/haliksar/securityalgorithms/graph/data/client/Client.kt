package me.haliksar.securityalgorithms.graph.data.client

import me.haliksar.securityalgorithms.graph.domain.entity.Graph
import me.haliksar.securityalgorithms.graph.domain.entity.Node

interface Client {

    fun shuffle(graph: Graph)
}

class ClientImpl : Client {

    override fun shuffle(graph: Graph) {
        graph.nodes.forEach(Node::generate)
    }
}