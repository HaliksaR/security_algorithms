package me.haliksar.securityalgorithms.graph.data.client

import me.haliksar.securityalgorithms.graph.domain.entity.Graph

interface Client {

    fun shuffle(graph: Graph)
}

class ClientImpl : Client {

    override fun shuffle(graph: Graph) {
        val colorList = graph.nodes.map { it.color }.shuffled()
        colorList.forEachIndexed { index, color ->
            graph.nodes[index].color = color
            graph.nodes[index].generate()
        }
    }
}