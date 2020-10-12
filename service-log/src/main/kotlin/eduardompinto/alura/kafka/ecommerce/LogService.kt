package eduardompinto.alura.kafka.ecommerce

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import java.util.regex.Pattern

class LogService

fun main() {
    kafkaService<String>(
        consumerGroup = LogService::class.java.simpleName,
        pattern = Pattern.compile("ECOMMERCE.*"),
        extraProperties = mapOf(
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java.name
        )
    ) {
        println("------------------------------------------")
        println("LOG: ${it.topic()}")
        println(it.key())
        println(it.value())
        println(it.partition())
        println(it.offset())
        println("------------------------------------------")
    }.use { it.run() }
}
