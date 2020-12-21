package me.haliksar.securityalgorithms.graph.data.file_manager

import com.beust.klaxon.Klaxon
import me.haliksar.securityalgorithms.graph.domain.entity.Graph
import java.io.File

interface FileManager<D> {
    suspend fun save(data: D, filePath: String)

    suspend fun get(filePath: String): D
}

class JsonGraphManagerImpl(
    private val klaxon: Klaxon,
) : FileManager<Graph> {

    override suspend fun save(data: Graph, filePath: String) {
        val json = klaxon.toJsonString(data)
        File(filePath).apply {
            createNewFile()
            writeText(json)
        }
    }

    override suspend fun get(filePath: String): Graph =
        klaxon.parse<Graph>(File(filePath))
            ?: throw Exception("Graph invalid")
}