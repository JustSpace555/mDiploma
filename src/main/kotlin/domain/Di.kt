package domain

import domain.usecase.RegisterNewSensorByOperatorUseCase
import domain.usecase.RegisterSensorPublicKeyUseCase
import org.koin.dsl.module

val domainModule = module {
    factory<RegisterNewSensorByOperatorUseCase> { RegisterNewSensorByOperatorUseCase(get()) }
    factory<RegisterSensorPublicKeyUseCase> { RegisterSensorPublicKeyUseCase(get()) }
}