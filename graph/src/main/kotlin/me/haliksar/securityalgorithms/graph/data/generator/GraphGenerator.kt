package me.haliksar.securityalgorithms.graph.data.generator

import me.haliksar.securityalgorithms.graph.domain.entity.Color
import me.haliksar.securityalgorithms.graph.domain.entity.Edge
import me.haliksar.securityalgorithms.graph.domain.entity.Graph
import me.haliksar.securityalgorithms.graph.domain.entity.Node

interface GraphGenerator {
    suspend fun generate(countNodes: Int): Graph
}

class GraphGeneratorImpl : GraphGenerator {

    override suspend fun generate(countNodes: Int): Graph {
        val nodes: List<Node> = (0..countNodes).shuffled().map {
            Node(it, Color.values().random())
        }
        val edges = mutableListOf<Edge>()
        for (i in 0 until countNodes - 1) {
            edges.add(Edge(nodes[i].id, nodes[i + 1].id))
        }
        return Graph(nodes, edges)
    }

}