package ru.otus.otuskotlin.mrosystem.springapp.config

import MrosAppSettings
import TaskRepoStub
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.otus.otuskotlin.marketplace.repo.inmemory.TaskRepoInMemory
import ru.otus.otuskotlin.mrosystem.biz.MrosTaskProcessor
import ru.otus.otuskotlin.mrosystem.common.MrosCorSettings
import ru.otus.otuskotlin.mrosystem.common.repo.ITaskRepository

@Configuration
class AppConfig {

    @Bean
    fun processor(corSettings: MrosCorSettings) = MrosTaskProcessor(corSettings)

    @Bean
    fun corSettings(
        @Qualifier("prodRepository") prodRepository: ITaskRepository?,
        @Qualifier("testRepository") testRepository: ITaskRepository,
        @Qualifier("stubRepository") stubRepository: ITaskRepository,
    ): MrosCorSettings = MrosCorSettings(
        //loggerProvider = loggerProvider(),
        repoStub = stubRepository,
        repoTest = testRepository,
        repoProd = prodRepository ?: testRepository,
    )

    @Bean
    fun appSettings(corSettings: MrosCorSettings, processor: MrosTaskProcessor) = MrosAppSettings(
        processor = processor,
        //logger = loggerProvider(),
        corSettings = corSettings
    )

    //@Bean(name = ["prodRepository"])
    //@ConditionalOnProperty(value = ["prod-repository"], havingValue = "sql")
    //fun prodRepository(sqlProperties: SqlProperties) = RepoTaskSQL(sqlProperties)

    @Bean
    fun testRepository() = TaskRepoInMemory()

    @Bean
    fun stubRepository() = TaskRepoStub()

}