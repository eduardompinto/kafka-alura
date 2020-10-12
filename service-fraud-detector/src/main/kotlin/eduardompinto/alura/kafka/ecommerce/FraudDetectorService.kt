package eduardompinto.alura.kafka.ecommerce

private const val NEW_ORDER_TOPIC = "ECOMMERCE_NEW_ORDER"
private const val REJECTED_ORDER_TOPIC = "ECOMMERCE_ORDER_REJECTED"
private const val APPROVED_ORDER_TOPIC = "ECOMMERCE_ORDER_APPROVED"

class FraudDetectorService {
    private val orderDispatcher: KafkaDispatcher<Order> = KafkaDispatcher()
    private val service = kafkaService<Order>(
        consumerGroup = FraudDetectorService::class.java.simpleName,
        topic = NEW_ORDER_TOPIC
    ) { record ->
        println("------------------------------------------")
        println("Processing new order, checking for fraud")
        println(record.key())
        println(record.value())
        println(record.partition())
        println(record.offset())
        Thread.sleep(3000)
        val order = record.value()
        if (isFraud(order)) {
            println("Order is a fraud!!!!! [$order]")
            orderDispatcher.send(REJECTED_ORDER_TOPIC, order.email, order)
        } else {
            println("Approved [$order]")
            orderDispatcher.send(APPROVED_ORDER_TOPIC, order.email, order)
        }
        println("Order processed")
        println("------------------------------------------")
    }

    fun run() = service.use { it.run() }

    private fun isFraud(order: Order) = order.amount > 4500.toBigDecimal()
}

fun main() {
    FraudDetectorService().run()
}
