package di

import data.repositories.implementations.LocationRepositoryImpl
import data.repositories.implementations.UserRepositoryImpl
import data.repositories.implementations.WeatherRepositoryImpl
import data.repositories.interfaces.LocationRepository
import data.repositories.interfaces.UserRepository
import data.repositories.interfaces.WeatherRepository
import org.koin.dsl.module.module

/**
 * Модуль для управления зависимостями репозиториев
 *
 * @author Keyrillanskiy
 * @since 16.01.2019, 21:57.
 */
val repositoriesModule = module {
    factory<UserRepository> { UserRepositoryImpl(get()) }
    factory<LocationRepository> { LocationRepositoryImpl(get()) }
    factory<WeatherRepository> { WeatherRepositoryImpl(get()) }
}