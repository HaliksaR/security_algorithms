package me.haliksar.securityalgorithms.graph.domain.repository

import me.haliksar.securityalgorithms.graph.domain.entity.Graph

interface GraphRepository {

    suspend fun getFromFile(filePath: String): Graph

    suspend fun generate(countNodes: Int): Graph

    suspend fun saveFromFile(graph: Graph, filePath: String)

    suspend fun show(graph: Graph)

    suspend fun processing(graph: Graph): Boolean
}