package data

import data.sensor.SensorRepository
import data.sensor.SensorTable
import data.transaction.TransactionRepository
import data.transaction.TransactionTable
import org.koin.dsl.module

val dataModule = module {
    single<SensorTable> { SensorTable }

    single<TransactionTable> { TransactionTable }

    single<Database> { Database(get(), get()) }

    single<SensorRepository> { SensorRepository(get()) }

    single<TransactionRepository> { TransactionRepository(get()) }

    single<DagTangleRepository> {
        DagTangleRepository(
            sensorRepository = get(),
            transactionRepository = get(),
            signature = get(),
        )
    }
}