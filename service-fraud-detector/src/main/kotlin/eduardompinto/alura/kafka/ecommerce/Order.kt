package eduardompinto.alura.kafka.ecommerce

import java.math.BigDecimal

data class Order(
    private val orderId: String,
    val email: String,
    val amount: BigDecimal,
)
