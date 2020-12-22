package me.haliksar.securityalgorithms.graph

import kotlinx.coroutines.runBlocking
import me.haliksar.securityalgorithms.graph.di.GraphModules
import me.haliksar.securityalgorithms.graph.domain.usecase.GenerateGraphUseCase
import me.haliksar.securityalgorithms.graph.domain.usecase.GetGraphUseCase
import me.haliksar.securityalgorithms.graph.domain.usecase.ProcessingGraphUseCase
import me.haliksar.securityalgorithms.graph.domain.usecase.SaveGraphUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin


class App : KoinComponent {

    companion object {

        // graph/src/main/resources/graph_data.json graph/src/main/resources/graph_out.json
        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            initKoin()
            App().launch(args)
        }

        private fun initKoin() {
            startKoin {
                modules(GraphModules)
            }
        }
    }

    suspend fun launch(args: Array<String>) {
        when {
            args.isEmpty() -> generateStrategy()
            args.size == 1 -> fromFileStrategy(args[0])
            args.size == 2 -> fromFilesStrategy(args[0], args[1])
        }
    }


    private suspend fun generateStrategy() {
        println("Generate...")
        val graph = get<GenerateGraphUseCase>().invoke(10000)
        get<SaveGraphUseCase>().invoke(graph, "graph/src/main/resources/graph_save.json")

        println("Processing...")
        if (get<ProcessingGraphUseCase>().invoke(graph)) {
            println("Valid Graph")
        }
    }

    private suspend fun fromFileStrategy(filePath: String) {
        println("Read file...")
        val graph = get<GetGraphUseCase>().invoke(filePath)


        println("Processing...")
        if (get<ProcessingGraphUseCase>().invoke(graph)) {
            println("Valid Graph")
        }
    }

    private suspend fun fromFilesStrategy(input: String, output: String) {
        println("Read file...")
        val graph = get<GetGraphUseCase>().invoke(input)

        println("Processing...")
        if (get<ProcessingGraphUseCase>().invoke(graph)) {
            println("Valid Graph")
            println("Save file...")
            get<SaveGraphUseCase>().invoke(graph, output)
        }
    }
}




