package me.haliksar.securityalgorithms.graph.domain.usecase

import me.haliksar.securityalgorithms.graph.domain.entity.Graph
import me.haliksar.securityalgorithms.graph.domain.repository.GraphRepository


class ProcessingGraphUseCase(
    private val repository: GraphRepository,
) {

    suspend operator fun invoke(graph: Graph) {
        repository.processing(graph)
    }
}