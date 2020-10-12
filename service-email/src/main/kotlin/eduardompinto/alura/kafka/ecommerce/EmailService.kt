package eduardompinto.alura.kafka.ecommerce

class EmailService
const val SEND_EMAIL_TOPIC = "ECOMMERCE_SEND_EMAIL"

fun main() {
    kafkaService<Email>(
        consumerGroup = EmailService::class.simpleName!!,
        topic = SEND_EMAIL_TOPIC
    ) {
        println("------------------------------------------")
        println("Send email")
        println(it.key())
        println(it.value())
        println(it.partition())
        println(it.offset())
        Thread.sleep(1000)
        println("Email sent")
        println("------------------------------------------")
    }.use { service -> service.run() }
}
