package me.haliksar.securityalgorithms.graph.di

import com.beust.klaxon.Klaxon
import me.haliksar.securityalgorithms.graph.data.client.Client
import me.haliksar.securityalgorithms.graph.data.client.ClientImpl
import me.haliksar.securityalgorithms.graph.data.datasource.GraphLocalDataSource
import me.haliksar.securityalgorithms.graph.data.datasource.GraphLocalDataSourceImpl
import me.haliksar.securityalgorithms.graph.data.file_manager.FileManager
import me.haliksar.securityalgorithms.graph.data.file_manager.JsonGraphManagerImpl
import me.haliksar.securityalgorithms.graph.data.generator.GraphGenerator
import me.haliksar.securityalgorithms.graph.data.generator.GraphGeneratorImpl
import me.haliksar.securityalgorithms.graph.data.repository.GraphRepositoryImpl
import me.haliksar.securityalgorithms.graph.data.server.Server
import me.haliksar.securityalgorithms.graph.data.server.ServerImpl
import me.haliksar.securityalgorithms.graph.domain.entity.Graph
import me.haliksar.securityalgorithms.graph.domain.repository.GraphRepository
import me.haliksar.securityalgorithms.graph.domain.usecase.GenerateGraphUseCase
import me.haliksar.securityalgorithms.graph.domain.usecase.GetGraphUseCase
import me.haliksar.securityalgorithms.graph.domain.usecase.ProcessingGraphUseCase
import me.haliksar.securityalgorithms.graph.domain.usecase.SaveGraphUseCase
import org.koin.dsl.module

private val generatorsModule = module {
    single<GraphGenerator> { GraphGeneratorImpl() }
}

private val parserModule = module {
    single { Klaxon() }
}

private val fileManagerModule = module {
    single<FileManager<Graph>> { JsonGraphManagerImpl(klaxon = get()) }
}

private val dataSourcesModule = module {
    single<GraphLocalDataSource> { GraphLocalDataSourceImpl(fileManager = get(), generator = get()) }
}

private val repositoriesModule = module {
    single<GraphRepository> {
        GraphRepositoryImpl(
            dataSource = get(),
            server = get(),
            client = get(),
        )
    }
}

private val useCasesModule = module {
    single { GenerateGraphUseCase(get()) }
    single { GetGraphUseCase(get()) }
    single { SaveGraphUseCase(get()) }
    single { ProcessingGraphUseCase(get()) }
}

private val clientModule = module {
    single<Client> { ClientImpl() }
}

private val serverModule = module {
    single<Server> { ServerImpl() }
}

val GraphModules = listOf(
    parserModule,
    generatorsModule,
    fileManagerModule,
    dataSourcesModule,
    repositoriesModule,
    useCasesModule,
    clientModule,
    serverModule,
)