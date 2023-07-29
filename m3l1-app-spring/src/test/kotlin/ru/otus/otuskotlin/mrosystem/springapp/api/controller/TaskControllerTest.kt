package ru.otus.otuskotlin.mrosystem.springapp.api.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import ru.otus.api.models.*
import ru.otus.otuskotlin.mrosystem.common.MrosContext
import ru.otus.otuskotlin.mrosystem.mappers.*
import ru.otus.otuskotlin.mrosystem.springapp.service.MrosTaskBlockingProcessor

@WebMvcTest(TaskController::class, OfferController::class)
class TaskControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @MockBean
    private lateinit var processor: MrosTaskBlockingProcessor

    @Test
    fun createTask() = testStubTask(
        "/task/create",
        TaskCreateRequest(),
        MrosContext().toTransportCreate()
    )

    @Test
    fun readTask() = testStubTask(
        "/task/read",
        TaskReadRequest(),
        MrosContext().toTransportRead()
    )

    @Test
    fun updateTask() = testStubTask(
        "/task/update",
        TaskUpdateRequest(),
        MrosContext().toTransportUpdate()
    )

    @Test
    fun deleteTask() = testStubTask(
        "/task/delete",
        TaskDeleteRequest(),
        MrosContext().toTransportDelete()
    )

    @Test
    fun searchTask() = testStubTask(
        "/task/search",
        TaskSearchRequest(),
        MrosContext().toTransportSearch()
    )

    @Test
    fun searchOffers() = testStubTask(
        "/task/offers",
        TaskOffersRequest(),
        MrosContext().toTransportOffers()
    )

    private fun <Req : Any, Res : Any> testStubTask(
        url: String,
        requestObj: Req,
        responseObj: Res,
    ) {
        val request = mapper.writeValueAsString(requestObj)
        val response = mapper.writeValueAsString(responseObj)

        mvc.perform(
            MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(response))
    }
}