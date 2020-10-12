package eduardompinto.alura.kafka.ecommerce

import java.sql.Connection
import java.sql.DriverManager

private const val NEW_ORDER_TOPIC = "ECOMMERCE_NEW_ORDER"

class CreateUserService {

    private val connection: Connection by lazy {
        val url = "jdbc:sqlite:build/users_database.db"
        DriverManager.getConnection(url).apply {
            createStatement().execute(
                """create table Users(
                    |uuid varchar(200) primary key,
                    |email varchar(200)
                    |)""".trimMargin()
            )
        }
    }

    private val service = kafkaService<Order>(
        consumerGroup = CreateUserService::class.java.simpleName,
        topic = NEW_ORDER_TOPIC
    ) { record ->
        println("------------------------------------------")
        println("Processing new order, checking for new user")
        println(record.value())
        val order = record.value()
        if (isNewUser(order.email)) {
            insertNewUser(order.email)
        }
        println("Order processed")
        println("------------------------------------------")
    }

    private fun insertNewUser(email: String) {
        connection.prepareStatement(
            "INSERT INTO Users (uuid, email) VALUES (?, ?)"
        ).apply {
            setString(1, "uuid")
            setString(2, "email")
        }.execute()
        println("User with uuid and $email was inserted")
    }

    private fun isNewUser(email: String): Boolean {
        val results = connection.prepareStatement(
            "select uuid from Users where email = ? limit 1"
        ).apply {
            setString(1, email)
        }.executeQuery()
        return !results.next()
    }

    fun run() = service.use { it.run() }
}

fun main() {
    CreateUserService().run()
}
