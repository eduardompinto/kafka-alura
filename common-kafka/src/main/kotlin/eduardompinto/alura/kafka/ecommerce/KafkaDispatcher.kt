package eduardompinto.alura.kafka.ecommerce

import org.apache.kafka.clients.producer.Callback
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import java.io.Closeable
import java.time.Duration
import java.util.Properties

class KafkaDispatcher<T> : Closeable {

    private val producer = KafkaProducer<String, T>(Properties().apply {
        this[BOOTSTRAP_SERVERS_CONFIG] = "127.0.0.1:9092"
        this[KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
        this[VALUE_SERIALIZER_CLASS_CONFIG] = GsonSerializer::class.java.name
    })

    private val callback = Callback { data, ex ->
        if (ex != null) {
            ex.printStackTrace()
        } else {
            with(data) {
                println(
                    "Sent to topic ${topic()}:::partition ${partition()}/ offset " +
                        "${offset()}/ timestamp ${timestamp()}"
                )
            }
        }
    }

    fun send(topic: String, key: String, value: T) {
        val record = ProducerRecord(topic, key, value)
        producer.send(record, callback).get()
    }

    fun send(topic: String, value: T) {
        val record = ProducerRecord(topic, value.hashCode().toString(), value)
        producer.send(record, callback).get()
    }

    override fun close() {
        this.producer.close(Duration.ofSeconds(10))
    }
}