package routing

import domain.usecase.RegisterNewSensorByOperatorUseCase
import io.ktor.server.routing.*

class OperatorRouting(
    private val registerNewSensorByOperatorUseCase: RegisterNewSensorByOperatorUseCase,
) {

    context(Routing)
    fun routing() {
        post("/operator/register/sensor") {
            registerNewSensorByOperatorUseCase()
        }
    }
}