package di

import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import presentation.screens.settings.SettingsViewModel

/**
 * Модуль для управления зависимостями экрана настроек
 *
 * @author Keyrillanskiy
 * @since 26.02.2019, 12:38.
 */
val settingsModule = module {
    viewModel { SettingsViewModel(get(), get(), get()) }
}