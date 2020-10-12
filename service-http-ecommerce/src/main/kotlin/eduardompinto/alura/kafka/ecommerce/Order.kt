package eduardompinto.alura.kafka.ecommerce

import java.math.BigDecimal
import java.util.UUID

data class Order(
    private val orderId: String = UUID.randomUUID().toString(),
    private val amount: BigDecimal = (Math.random() * 10000 + 1).toBigDecimal(),
    val email: String = "${Math.random()}@email.com"
)
