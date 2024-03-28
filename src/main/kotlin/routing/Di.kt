package routing

import org.koin.dsl.module

val routingModule = module {
    single<SensorRouting> {
        SensorRouting(
            registerSensorPublicKeyUseCase = get(),
            registerNewSensorByOperatorUseCase = get(),
        )
    }
}