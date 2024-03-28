package domain

import domain.usecase.RegisterNewSensorByOperatorUseCase
import domain.usecase.RegisterNewTransactionUseCase
import domain.usecase.RegisterSensorPublicKeyUseCase
import domain.usecase.RequestTipsUseCase
import org.koin.dsl.module

val domainModule = module {
    factory<RegisterNewSensorByOperatorUseCase> { RegisterNewSensorByOperatorUseCase(get()) }
    factory<RegisterSensorPublicKeyUseCase> { RegisterSensorPublicKeyUseCase(get()) }
    factory<RequestTipsUseCase> { RequestTipsUseCase(get()) }
    single<SensorSignature> { SensorSignature() }
    factory {
        RegisterNewTransactionUseCase(
            sensorRepository = get(),
            dagTangleRepository = get(),
            signature = get(),
        )
    }
}