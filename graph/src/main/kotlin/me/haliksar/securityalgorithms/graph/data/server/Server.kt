package me.haliksar.securityalgorithms.graph.data.server

import me.haliksar.securityalgorithms.graph.domain.entity.Edge
import me.haliksar.securityalgorithms.graph.domain.entity.Graph
import me.haliksar.securityalgorithms.libs.modexp.modExp


interface Server {
    fun chooseEdge(graph: Graph): Edge
    fun check(graph: Graph, current: Edge): Boolean
}

class ServerImpl : Server {

    override fun chooseEdge(graph: Graph): Edge = graph.edges.random()

    override fun check(graph: Graph, current: Edge): Boolean {
        val node1 = graph.nodes[current.nodeId1]
        val node2 = graph.nodes[current.nodeId2]
        val val1 = node1.getZ().modExp(node1.getC(), node1.getN()) and 7
        val val2 = node2.getZ().modExp(node2.getC(), node2.getN()) and 7
        if (val1 != val2) {
            println("Node[${current.nodeId1}] != Node[${current.nodeId2}]")
        } else {
            println("Node[${current.nodeId1}] == Node[${current.nodeId2}]")
        }
        return val1 == val2
    }
}