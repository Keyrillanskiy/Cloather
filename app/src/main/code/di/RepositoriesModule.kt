package di

import data.repositories.implementations.UserRepositoryImpl
import data.repositories.interfaces.UserRepository
import org.koin.dsl.module.module

/**
 * Модуль для управления зависимостями репозиториев
 *
 * @author Keyrillanskiy
 * @since 16.01.2019, 21:57.
 */

val repositoriesModule = module {
    factory<UserRepository> { UserRepositoryImpl(get()) }
}