package eduardompinto.alura.kafka.ecommerce

const val NEW_ORDER_TOPIC = "ECOMMERCE_NEW_ORDER"
const val SEND_EMAIL_TOPIC = "ECOMMERCE_SEND_EMAIL"

fun main() {
    KafkaDispatcher<Order>().use { orderDispatcher ->
        KafkaDispatcher<Email>().use { emailDispatcher ->
            (1 until 10).forEach { _ ->
                sendNewOrderMessage(orderDispatcher)
                sendEmailMessage(emailDispatcher)
            }
        }
    }
}

private fun sendNewOrderMessage(dispatcher: KafkaDispatcher<Order>) = with(Order()) {
    dispatcher.send(topic = NEW_ORDER_TOPIC, value = this)
}

private fun sendEmailMessage(dispatcher: KafkaDispatcher<Email>) = with(
    Email(
        subject = "Your order!",
        body = "Thank you for your order! We are processing your order!"
    )
) {
    dispatcher.send(topic = SEND_EMAIL_TOPIC, value = this)
}
