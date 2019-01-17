package di

import data.network.CloatherHttpClient
import org.koin.dsl.module.module
import utils.SchedulersFacade

/**
 * Модуль для управления общими зависимостями приложения
 *
 * @author Keyrillanskiy
 * @since 16.01.2019, 21:56.
 */

val commonModule = module {
    single { CloatherHttpClient() }
    single { SchedulersFacade() }
}