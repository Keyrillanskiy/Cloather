package di

import data.mappers.implementations.LocationRequestDataMapperImpl
import data.mappers.implementations.UserMapperImpl
import data.mappers.interfaces.LocationRequestDataMapper
import data.useCases.implementations.LocationUseCaseImpl
import data.useCases.implementations.UserUseCaseImpl
import data.useCases.interfaces.LocationUseCase
import data.useCases.interfaces.UserUseCase
import org.koin.dsl.module.module

/**
 * Модуль для управления зависимостями useCases
 *
 * @author Keyrillanskiy
 * @since 18.01.2019, 21:16.
 */

val useCasesModule = module {
    factory<UserUseCase> { UserUseCaseImpl(get(), UserMapperImpl()) }
    factory<LocationUseCase> { LocationUseCaseImpl(get(), LocationRequestDataMapperImpl()) }
}
