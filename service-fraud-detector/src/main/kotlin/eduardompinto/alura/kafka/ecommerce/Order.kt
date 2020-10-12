package eduardompinto.alura.kafka.ecommerce

import java.math.BigDecimal

data class Order(
    val userId: String,
    private val orderId: String,
    val amount: BigDecimal,
)