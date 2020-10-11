package eduardompinto.alura.kafka.ecommerce

class FraudDetectorService

const val NEW_ORDER_TOPIC = "ECOMMERCE_NEW_ORDER"

fun main() {
    kafkaService<Order>(
        consumerGroup = FraudDetectorService::class.java.simpleName,
        topic = NEW_ORDER_TOPIC
    ) {
        println("------------------------------------------")
        println("Processing new order, checking for fraud")
        println(it.key())
        println(it.value())
        println(it.partition())
        println(it.offset())
        Thread.sleep(300)
        println("Order processed")
        println("------------------------------------------")
    }.use { service -> service.run() }
}



