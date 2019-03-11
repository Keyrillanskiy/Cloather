package presentation.screens.wardrobe

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.keyrillanskiy.cloather.R
import domain.models.responses.toCategoryItem
import domain.models.responses.toThingItem
import extensions.reObserve
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import presentation.common.Failure
import presentation.common.Loading
import presentation.common.Success
import utils.NetUtils

/**
 * Экран выбора гардероба.
 *
 * @author Keyrillanskiy
 * @since 27.02.2019, 12:22.
 */
class WardrobeFragment : Fragment() {

    private val viewModel by sharedViewModel<WardrobeViewModel>()
    private lateinit var viewHolder: WardrobeViewHolder
    private var parentInteractor: WardrobeInteractor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.observe()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_wardrobe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewHolder = WardrobeViewHolder(view).setup {
            onBackClick = {
                viewModel.fetchCategories()
                viewHolder.showDefaultToolbar()
            }
            onCategoryClick = { category -> viewModel.fetchThings(category.id) }
            onThingSelected = { thingItem -> viewModel.updateThing(thingItem) }
        }

        NetUtils.withNetConnection(
            onSuccess = { viewModel.fetchCategories() },
            onError = { parentInteractor?.onInternetError() })
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        parentInteractor = context as? WardrobeInteractor ?: throw IllegalArgumentException(
            "Invalid parent attached: $context"
        )
    }

    override fun onDetach() {
        parentInteractor = null
        super.onDetach()
    }

    private fun WardrobeViewModel.observe() {
        categoriesLiveData.reObserve(this@WardrobeFragment, Observer { response ->
            when (response) {
                is Loading -> parentInteractor?.onShowLoading()
                is Success -> {
                    parentInteractor?.onHideLoading()
                    viewHolder.showCategories(response.value.map { it.toCategoryItem() })
                }
                is Failure -> {
                    parentInteractor?.onHideLoading()
                }
            }
        })

        thingsLiveData.reObserve(this@WardrobeFragment, Observer { response ->
            when (response) {
                is Loading -> parentInteractor?.onShowLoading()
                is Success -> {
                    val wardrobeThings = response.value.first
                    val thingsByCategory = response.value.second

                    val thingsToShow = thingsByCategory.map { thingByCategory ->
                        val isInWardrobe = wardrobeThings.any { wardrobeThing ->
                            wardrobeThing.id == thingByCategory.id
                        }
                        thingByCategory.toThingItem(isInWardrobe, viewModel.gender)
                    }

                    viewHolder.showThings(thingsToShow)

                    parentInteractor?.onHideLoading()
                }
                is Failure -> parentInteractor?.onHideLoading()
            }
        })
    }

}

interface WardrobeInteractor {
    fun onShowLoading()
    fun onHideLoading()
    fun onInternetError()
}