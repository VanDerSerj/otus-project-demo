
import ru.otus.api.models.*
import ru.otus.otuskotlin.mrosystem.api.apiMapper
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseSerializationTest {
    private val response = TaskCreateResponse(
        requestId = "123",
        task = TaskResponseObject(
            title = "task title",
            description = "task description",
            taskType = PerformSide.ENGINEER,
            visibility = TaskVisibility.PUBLIC,
        )
    )

    @Test
    fun serialize() {
        val json = apiMapper.writeValueAsString(response)

        assertContains(json, Regex("\"title\":\\s*\"task title\""))
        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiMapper.writeValueAsString(response)
        val obj = apiMapper.readValue(json, IResponse::class.java) as TaskCreateResponse

        assertEquals(response, obj)
    }
}
