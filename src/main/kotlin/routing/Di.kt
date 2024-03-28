package routing

import org.koin.dsl.module

val routingModule = module {
    single<OperatorRouting> {
        OperatorRouting(
            registerNewSensorByOperatorUseCase = get(),
        )
    }
    single<SensorRouting> {
        SensorRouting(
            registerSensorPublicKeyUseCase = get(),
            requestTipsUseCase = get(),
            registerNewTransactionUseCase = get(),
        )
    }
}