package data

import data.sensor.SensorRepository
import data.sensor.SensorTable
import org.koin.dsl.module

val dataModule = module {
    single<SensorTable> { SensorTable() }
    single<Database> { Database(get()) }
    single<SensorRepository> { SensorRepository(get()) }
}