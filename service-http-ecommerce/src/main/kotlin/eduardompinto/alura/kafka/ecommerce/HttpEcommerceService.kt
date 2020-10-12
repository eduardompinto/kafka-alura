package eduardompinto.alura.kafka.ecommerce

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder

class HttpEcommerceService {

    fun run() {
        Server(8080).apply {
            handler = ServletContextHandler().apply {
                contextPath = "/"
                addServlet(ServletHolder(NewOrderServlet()), "/new")
            }
            start()
            join()
        }
    }
}

fun main() {
    HttpEcommerceService().run()
}
