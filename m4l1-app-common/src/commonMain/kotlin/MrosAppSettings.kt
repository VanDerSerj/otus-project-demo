import ru.otus.otuskotlin.mrosystem.biz.MrosTaskProcessor
import ru.otus.otuskotlin.mrosystem.common.MrosCorSettings
import ru.otus.otuskotlin.mrosystem.logging.common.MpLoggerProvider

data class MrosAppSettings(
    val appUrls: List<String> = emptyList(),
    val corSettings: MrosCorSettings = MrosCorSettings(),
    val processor: MrosTaskProcessor = MrosTaskProcessor(settings = corSettings),
    val logger: MpLoggerProvider = MpLoggerProvider()
)
