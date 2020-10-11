package eduardompinto.alura.kafka.ecommerce

const val NEW_ORDER_TOPIC = "ECOMMERCE_NEW_ORDER"
const val SEND_EMAIL_TOPIC = "ECOMMERCE_SEND_EMAIL"

fun main() {
    KafkaDispatcher<Order>().use { dispatcher ->
        (1 until 10).forEach { _ ->
            sendNewOrderMessage(dispatcher)
        }
    }
    KafkaDispatcher<Email>().use { dispatcher ->
        (1 until 10).forEach { _ ->
            sendEmailMessage(dispatcher)
        }
    }
}

private fun sendNewOrderMessage(dispatcher: KafkaDispatcher<Order>) {
    val value = Order()
    val key = value.hashCode().toString()
    dispatcher.send(NEW_ORDER_TOPIC, key, value)
}

private fun sendEmailMessage(dispatcher: KafkaDispatcher<Email>) {
    val email = Email(
        subject = "Your order!",
        body = "Thank you for your order! We are processing your order!"
    )
    dispatcher.send(SEND_EMAIL_TOPIC, email.hashCode().toString(), email)
}

