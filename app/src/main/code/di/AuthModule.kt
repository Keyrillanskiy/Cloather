package di

import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import presentation.screens.auth.AuthViewModel

/**
 * Модуль для управления зависимостями экрана авторизации.
 *
 * @author Keyrillanskiy
 * @since 16.01.2019, 10:55.
 */
val authModule = module {
    viewModel { AuthViewModel(get(), get(), get()) }
}