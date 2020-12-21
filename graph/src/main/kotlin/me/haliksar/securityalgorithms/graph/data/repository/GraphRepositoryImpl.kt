package me.haliksar.securityalgorithms.graph.data.repository

import me.haliksar.securityalgorithms.graph.data.client.Client
import me.haliksar.securityalgorithms.graph.data.datasource.GraphLocalDataSource
import me.haliksar.securityalgorithms.graph.data.server.Server
import me.haliksar.securityalgorithms.graph.domain.entity.Graph
import me.haliksar.securityalgorithms.graph.domain.repository.GraphRepository

class GraphRepositoryImpl(
    private val dataSource: GraphLocalDataSource,
    private val server: Server,
    private val client: Client,
) : GraphRepository {

    override suspend fun getFromFile(filePath: String): Graph =
        dataSource.getFromFile(filePath)

    override suspend fun generate(countNodes: Int): Graph =
        dataSource.generate(countNodes)

    override suspend fun saveFromFile(graph: Graph, filePath: String) {
        dataSource.saveFromFile(graph, filePath)
    }

    override suspend fun show(graph: Graph) {
        dataSource.show(graph)
    }

    override suspend fun processing(graph: Graph) {
        repeat(graph.edges.size) {
            client.shuffle(graph)
            val edge = server.chooseEdge(graph)
            if (server.check(graph, edge)) {
                return
            }
        }
    }
}