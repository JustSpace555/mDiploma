package routing

import domain.usecase.RegisterNewSensorByOperatorUseCase
import domain.usecase.SubscribeToApprovedTransactionUpdatedUseCase
import domain.usecase.SubscribeToNewTransactionUpdatesUseCase
import io.ktor.server.routing.*
import io.ktor.server.sse.*

class OperatorRouting(
    private val registerNewSensorByOperatorUseCase: RegisterNewSensorByOperatorUseCase,
    private val subscribeToNewTransactionUpdatesUseCase: SubscribeToNewTransactionUpdatesUseCase,
    private val subscribeToApprovedTransactionUpdatedUseCase: SubscribeToApprovedTransactionUpdatedUseCase,
) {

    context(RootRoute)
    fun routing() {
        post("/operator/register/sensor") {
            registerNewSensorByOperatorUseCase()
        }

        sse("/operator/subscribe/transaction/new") {
            subscribeToNewTransactionUpdatesUseCase()
        }

        sse("/operator/subscribe/transaction/approved") {
            subscribeToApprovedTransactionUpdatedUseCase()
        }
    }
}