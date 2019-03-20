package di

import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import presentation.screens.main.MainViewModel

/**
 * Модуль для управления зависимостями главного экрана
 *
 * @author Keyrillanskiy
 * @since 19.01.2019, 15:44.
 */
val mainModule = module {
    viewModel { MainViewModel(get(), get(), get(), get(), get(), get()) }
}