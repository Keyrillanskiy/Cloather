package di

import data.network.apiSpecs.LocationApiSpec
import data.network.apis.LocationApi
import org.koin.dsl.module.module

/**
 * Модуль для управления зависимостями API.
 *
 * @author Keyrillanskiy
 * @since 09.02.2019, 15:30.
 */
val apisModule = module {
    factory<LocationApiSpec> { LocationApi() }
}
