package me.haliksar.securityalgorithms.graph.domain.usecase

import me.haliksar.securityalgorithms.graph.domain.entity.Graph
import me.haliksar.securityalgorithms.graph.domain.repository.GraphRepository


class GetGraphUseCase(
    private val repository: GraphRepository,
) {

    suspend operator fun invoke(filePath: String): Graph =
        repository.getFromFile(filePath)
}