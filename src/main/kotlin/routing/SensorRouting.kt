package routing

import io.ktor.server.routing.*
import domain.usecase.RegisterNewTransactionUseCase
import domain.usecase.RegisterSensorPublicKeyUseCase
import domain.usecase.RequestTipsUseCase

class SensorRouting(
    private val registerSensorPublicKeyUseCase: RegisterSensorPublicKeyUseCase,
    private val requestTipsUseCase: RequestTipsUseCase,
    private val registerNewTransactionUseCase: RegisterNewTransactionUseCase,
) {

    context(RootRoute)
    fun routing() {
        post("/sensor/register/{id}") {
            registerSensorPublicKeyUseCase()
        }

        get("/sensor/tips") {
            requestTipsUseCase()
        }

        post("/sensor/newTransaction") {
            registerNewTransactionUseCase()
        }
    }
}