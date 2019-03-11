package di

import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import presentation.screens.wardrobe.WardrobeViewModel

/**
 * @author Keyrillanskiy
 * @since 01.03.2019, 11:19.
 */
val wardrobeModule = module {
    viewModel { WardrobeViewModel(get(), get(), get()) }
}