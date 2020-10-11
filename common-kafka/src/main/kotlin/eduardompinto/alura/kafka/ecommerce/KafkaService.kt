package eduardompinto.alura.kafka.ecommerce

import eduardompinto.alura.kafka.ecommerce.GsonDeserializer.Companion.TYPE_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.CLIENT_ID_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_RECORDS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import java.io.Closeable
import java.time.Duration
import java.util.Properties
import java.util.UUID
import java.util.function.Consumer
import java.util.regex.Pattern

inline fun <reified T> kafkaService(
    consumerGroup: String,
    topic: String? = null,
    pattern: Pattern? = null,
    extraProperties: Map<String, String>? = null,
    parse: Consumer<ConsumerRecord<String, T>>,
): KafkaService<T> {
    return KafkaService(
        consumerGroup = consumerGroup,
        type = T::class.java,
        topic = topic,
        pattern = pattern,
        parse = parse,
        extraProperties = extraProperties ?: mapOf(TYPE_CONFIG to T::class.java.name)
    )
}

class KafkaService<T>(
    private val consumerGroup: String,
    private val type: Class<T>,
    topic: String? = null,
    pattern: Pattern? = null,
    extraProperties: Map<String, String> = emptyMap(),
    private val parse: Consumer<ConsumerRecord<String, T>>,
) : Closeable {

    private val consumer: KafkaConsumer<String, T>

    init {
        val properties = Properties().apply {
            this[BOOTSTRAP_SERVERS_CONFIG] = "127.0.0.1:9092"
            this[KEY_DESERIALIZER_CLASS_CONFIG] =
                StringDeserializer::class.java.name
            this[VALUE_DESERIALIZER_CLASS_CONFIG] =
                GsonDeserializer::class.java.name
            this[GROUP_ID_CONFIG] = consumerGroup
            this[CLIENT_ID_CONFIG] = "${UUID.randomUUID()}"
            this[MAX_POLL_RECORDS_CONFIG] =
                "1" // Common config, to optimize the number of commits
            this.putAll(extraProperties)
        }
        consumer = KafkaConsumer<String, T>(properties)
        if (topic != null) consumer.subscribe(listOf(topic))
        if (pattern != null) consumer.subscribe(pattern)
    }

    fun run() {
        while (true) {
            val records = consumer.poll(Duration.ofMillis(100))
            records.apply {
                if (!isEmpty) println("Records ${this.count()} found")
            }.forEach { record ->
                parse.accept(record)
            }
        }
    }

    override fun close() {
        this.consumer.close(Duration.ofSeconds(10))
    }

}