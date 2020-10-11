package eduardompinto.alura.kafka.ecommerce

import com.google.gson.GsonBuilder
import org.apache.kafka.common.serialization.Deserializer


class GsonDeserializer<T> : Deserializer<T> {

    private val gson = GsonBuilder().create()
    private lateinit var type: Class<T>

    companion object {
        const val TYPE_CONFIG = "eduardompinto.alura.kafka.GsonDeserializer.TypeConfig"
    }

    override fun deserialize(topic: String, data: ByteArray): T {
        return gson.fromJson(String(data), type)
    }

    override fun configure(configs: MutableMap<String, *>, isKey: Boolean) {
        val typeName =
            configs!!.getOrDefault(TYPE_CONFIG, String::class.java.name) as String
        type = Class.forName(typeName) as Class<T>
    }

}
