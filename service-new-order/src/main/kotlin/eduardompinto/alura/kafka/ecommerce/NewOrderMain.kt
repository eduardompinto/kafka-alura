package eduardompinto.alura.kafka.ecommerce

const val NEW_ORDER_TOPIC = "ECOMMERCE_NEW_ORDER"
const val SEND_EMAIL_TOPIC = "ECOMMERCE_SEND_EMAIL"

class NewOrderMain {

    private val order = Order()
    private val email = Email(
        subject = "Your order!",
        body = "Thank you for your order! We are processing your order!"
    )

    fun run() {
        KafkaDispatcher<Order>().use { orderDispatcher ->
            KafkaDispatcher<Email>().use { emailDispatcher ->
                (1 until 10).forEach { _ ->
                    sendNewOrderMessage(orderDispatcher)
                    sendEmailMessage(emailDispatcher)
                }
            }
        }
    }

    private fun sendEmailMessage(dispatcher: KafkaDispatcher<Email>) {
        dispatcher.send(topic = SEND_EMAIL_TOPIC, value = email)
    }

    private fun sendNewOrderMessage(dispatcher: KafkaDispatcher<Order>) {
        dispatcher.send(
            topic = NEW_ORDER_TOPIC,
            key = order.email,
            value = order
        )
    }
}

fun main() {
    NewOrderMain().run()
}
