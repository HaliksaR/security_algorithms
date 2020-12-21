package me.haliksar.securityalgorithms.graph.data.datasource

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.haliksar.securityalgorithms.graph.data.file_manager.FileManager
import me.haliksar.securityalgorithms.graph.data.generator.GraphGenerator
import me.haliksar.securityalgorithms.graph.domain.entity.Graph

interface GraphLocalDataSource {

    suspend fun getFromFile(filePath: String): Graph

    suspend fun generate(countNodes: Int): Graph

    suspend fun saveFromFile(graph: Graph, filePath: String)

    suspend fun show(graph: Graph)
}

class GraphLocalDataSourceImpl(
    private val fileManager: FileManager<Graph>,
    private val generator: GraphGenerator,
) : GraphLocalDataSource {

    override suspend fun getFromFile(filePath: String): Graph =
        withContext(Dispatchers.IO) {
            fileManager.get(filePath)
        }

    override suspend fun saveFromFile(graph: Graph, filePath: String) =
        withContext(Dispatchers.IO) {
            fileManager.save(graph, filePath)
        }

    override suspend fun generate(countNodes: Int): Graph =
        withContext(Dispatchers.IO) {
            generator.generate(countNodes)
        }

    override suspend fun show(graph: Graph) {
        println(graph)
    }
}



