package eduardompinto.alura.kafka.ecommerce

import java.math.BigDecimal

data class Order(
    private val orderId: String,
    private val amount: BigDecimal,
    val email: String
)
