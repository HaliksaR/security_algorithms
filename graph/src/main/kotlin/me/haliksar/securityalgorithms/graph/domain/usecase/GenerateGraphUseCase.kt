package me.haliksar.securityalgorithms.graph.domain.usecase

import me.haliksar.securityalgorithms.graph.domain.entity.Graph
import me.haliksar.securityalgorithms.graph.domain.repository.GraphRepository


class GenerateGraphUseCase(
    private val repository: GraphRepository,
) {

    suspend operator fun invoke(countNodes: Int): Graph =
        repository.generate(countNodes)
}