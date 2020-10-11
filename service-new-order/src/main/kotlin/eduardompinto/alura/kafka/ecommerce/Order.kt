package eduardompinto.alura.kafka.ecommerce

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.UUID

data class Order(
    private val userId: String = UUID.randomUUID().toString(),
    private val orderId: String = UUID.randomUUID().toString(),
    private val amount: BigDecimal = (Math.random() * 5000 + 1).toBigDecimal().setScale(2, RoundingMode.HALF_UP)
)