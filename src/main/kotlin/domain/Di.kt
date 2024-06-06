package domain

import domain.usecase.*
import org.koin.dsl.module

val domainModule = module {
    factory<RegisterNewSensorByOperatorUseCase> { RegisterNewSensorByOperatorUseCase(get()) }

    factory<RegisterSensorPublicKeyUseCase> { RegisterSensorPublicKeyUseCase(get()) }

    factory<RequestTipsUseCase> { RequestTipsUseCase(get()) }

    single<SensorSignature> { SensorSignature() }

    factory<RegisterNewTransactionUseCase> { RegisterNewTransactionUseCase(get()) }

    factory<SubscribeToApprovedTransactionUpdatedUseCase> {
        SubscribeToApprovedTransactionUpdatedUseCase(get())
    }

    factory<SubscribeToNewTransactionUpdatesUseCase> {
        SubscribeToNewTransactionUpdatesUseCase(get())
    }
}