package eduardompinto.alura.kafka.ecommerce

import org.eclipse.jetty.http.HttpStatus
import javax.servlet.ServletException
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

const val NEW_ORDER_TOPIC = "ECOMMERCE_NEW_ORDER"
const val SEND_EMAIL_TOPIC = "ECOMMERCE_SEND_EMAIL"

class NewOrderServlet : HttpServlet() {
    private val orderDispatcher = KafkaDispatcher<Order>()
    private val emailDispatcher = KafkaDispatcher<Email>()

    override fun destroy() {
        super.destroy()
        orderDispatcher.close()
        emailDispatcher.close()
    }

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        val email = Email(
            subject = "Your order!",
            body = "Thank you for your order! We are processing your order!"
        )
        try {
            sendNewOrderMessage(
                Order(
                    email = req.getParameter("email"),
                    amount = req.getParameter("amount").toBigDecimal()
                )
            )
            sendEmailMessage(email)
            val message = "New order sent successfully"
            println(message)
            resp.status = HttpStatus.OK_200
            resp.writer.println(message)
        } catch (ex: Exception) {
            throw ServletException(ex)
        }
    }

    private fun sendEmailMessage(email: Email) {
        emailDispatcher.send(topic = SEND_EMAIL_TOPIC, value = email)
    }

    private fun sendNewOrderMessage(order: Order) {
        orderDispatcher.send(
            topic = NEW_ORDER_TOPIC,
            key = order.email,
            value = order
        )
    }
}
