package test

import fixture.client.Client
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldExist
import io.kotest.matchers.collections.shouldExistInOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import ru.otus.api.models.TaskSearchFilter
import test.action.v.*
import ru.otus.api.models.*

fun FunSpec.testApi(client: Client, prefix: String = "") {
    context("${prefix} prod") {
        test("Create Task ok") {
            client.createTask(mode = prod)
        }

        test("Read Task ok") {
            val created = client.createTask(mode = prod)
            client.readTask(created.id, mode = prod).asClue {
                it shouldBe created
            }
        }

        test("Update Task ok") {
            val created = client.createTask(mode = prod)
            client.updateTask(
                created.id,
                created.lock,
                TaskUpdateObject(title = "Change Nut", description = someCreateTask.description),
                mode = prod
            )
            client.readTask(created.id, mode = prod) {
                it.task?.title shouldBe "Change Nut"
                it.task?.description shouldBe someCreateTask.description
            }
        }

        test("Delete Task ok") {
            val created = client.createTask(mode = prod)
            client.deleteTask(created.id, created.lock, mode = prod)
            client.readTask(created.id, mode = prod) {
                it should haveError("not-found")
            }
        }

        test("Search Task ok") {
            client.createTask(someCreateTask.copy(title = "Change Bolt"), mode = prod)
            client.createTask(someCreateTask.copy(title = "Change Nut"), mode = prod)

            withClue("Search Change") {
                val results = client.searchTask(search = TaskSearchFilter(searchString = "Change"), mode = prod)
                results shouldHaveSize 2
                results shouldExist { it.title == "Change Bolt" }
                results shouldExist { it.title == "Change Nut" }
            }

            withClue("Search Bolt") {
                val results = client.searchTask(search = TaskSearchFilter(searchString = "Bolt"), mode = prod)
                results.shouldExistInOrder({ it.title == "Change Bolt" })
            }

            withClue("Search NotExisted") {
                val results = client.searchTask(search = TaskSearchFilter(searchString = "NotExisted"), mode = prod)
                results shouldHaveSize 0
            }
        }

        test("Offer Task ok") {
            val supply = client.createTask(someCreateTask.copy(title = "Some Bolt", taskType = PerformSide.MECHANIC), mode = prod)
            val demand = client.createTask(someCreateTask.copy(title = "Some Bolt", taskType = PerformSide.ENGINEER), mode = prod)

            withClue("Find offer for Mechanic") {
                client.offersTask(supply.id, mode = prod)
                    .shouldExistInOrder({it.id == demand.id })
            }

            withClue("Find offer for Engineer") {
                client.offersTask(demand.id, mode = prod)
                    .shouldExistInOrder({it.id == supply.id })
            }
        }
    }

}