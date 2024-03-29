package routing

import domain.usecase.RegisterNewSensorByOperatorUseCase
import domain.usecase.SubscribeToApprovedTransactionUpdatedUseCase
import domain.usecase.SubscribeToNewTransactionUpdatesUseCase
import io.ktor.server.routing.*

class OperatorRouting(
    private val registerNewSensorByOperatorUseCase: RegisterNewSensorByOperatorUseCase,
    private val subscribeToApprovedTransactionUpdatedUseCase: SubscribeToApprovedTransactionUpdatedUseCase,
    private val subscribeToNewTransactionUpdatesUseCase: SubscribeToNewTransactionUpdatesUseCase,
) {

    context(Routing)
    fun routing() {
        post("/operator/register/sensor") {
            registerNewSensorByOperatorUseCase()
        }

        get("/operator/subscribe/transaction/new") {
            subscribeToNewTransactionUpdatesUseCase()
        }

        get("/operator/subscribe/transaction/approved") {
            subscribeToApprovedTransactionUpdatedUseCase()
        }
    }
}