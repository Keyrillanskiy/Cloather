package di

import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import presentation.screens.gender.GenderViewModel

/**
 * Модуль для управления зависимостями экрана выбора пола
 *
 * @author Keyrillanskiy
 * @since 19.01.2019, 17:42.
 */
val genderModule = module {
    viewModel { GenderViewModel(get(), get(), get()) }
}