package test

import fixture.client.Client
import io.kotest.assertions.asClue
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import ru.otus.api.models.PerformSide
import ru.otus.api.models.TaskSearchFilter
import test.action.v.*
import ru.otus.api.models.*


fun FunSpec.testStubApi(client: Client, prefix: String = "") {
    context("${prefix} stub") {
        test("Create Task ok") {
            client.createTask()
        }

        test("Read Task ok") {
            val created = client.createTask()
            client.readTask(created.id).asClue {
                it shouldBe created
            }
        }

        test("Update Task ok") {
            val created = client.createTask()
            client.updateTask(created.id, created.lock, TaskUpdateObject(title = "Selling Nut"))
            client.readTask(created.id) {
                // TODO раскомментировать, когда будет реальный реп
                //it.task?.title shouldBe "Selling Nut"
                //it.task?.description shouldBe someCreateTask.description
            }
        }

        test("Delete Task ok") {
            val created = client.createTask()
            client.deleteTask(created.id, created.lock)
            client.readTask(created.id) {
                // it should haveError("not-found") TODO раскомментировать, когда будет реальный реп
            }
        }

        test("Search Task ok") {
            val created1 = client.createTask(someCreateTask.copy(title = "Selling Bolt"))
            val created2 = client.createTask(someCreateTask.copy(title = "Selling Nut"))

            withClue("Search Engineer task") {
                val results = client.searchTask(search = TaskSearchFilter(searchString = "Selling"))
                // TODO раскомментировать, когда будет реальный реп
                // results shouldHaveSize 2
                // results shouldExist { it.title == "Selling Bolt" }
                // results shouldExist { it.title == "Selling Nut" }
            }

            withClue("Search Bolt") {
                client.searchTask(search = TaskSearchFilter(searchString = "Bolt"))
                // TODO раскомментировать, когда будет реальный реп
                // .shouldExistInOrder({ it.title == "Selling Bolt" })
            }
        }

        test("Offer Task ok") {
            val mechanic = client.createTask(someCreateTask.copy(title = "Some Bolt", taskType = PerformSide.MECHANIC))
            val engineer = client.createTask(someCreateTask.copy(title = "Some Bolt", taskType = PerformSide.ENGINEER))

            withClue("Find offer for Mechanic") {
                client.offersTask(mechanic.id)
                // TODO раскомментировать, когда будет реальный реп
                // .shouldExistInOrder({it.id == demand.id })
            }

            withClue("Find offer for Engineer") {
                client.offersTask(engineer.id)
                // TODO раскомментировать, когда будет реальный реп
                // .shouldExistInOrder({it.id == supply.id })
            }
        }
    }

}