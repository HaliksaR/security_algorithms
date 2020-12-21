package me.haliksar.securityalgorithms.graph.domain.usecase

import me.haliksar.securityalgorithms.graph.domain.entity.Graph
import me.haliksar.securityalgorithms.graph.domain.repository.GraphRepository


class SaveGraphUseCase(
    private val repository: GraphRepository,
) {

    suspend operator fun invoke(graph: Graph, filePath: String) {
        repository.saveFromFile(graph, filePath)
    }
}