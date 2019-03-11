package presentation.screens.wardrobe

import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.github.keyrillanskiy.cloather.R
import kotlinx.android.synthetic.main.fragment_wardrobe.view.*
import presentation.common.BaseViewHolder

/**
 * @author Keyrillanskiy
 * @since 01.03.2019, 11:01.
 */
class WardrobeViewHolder(private val rootView: View) : BaseViewHolder(rootView) {

    var onBackClick: (() -> Unit)? = null
    var onCategoryClick: ((categoryItem: CategoryItem) -> Unit)? = null
    var onThingSelected: ((thingItem: ThingItem) -> Unit)? = null

    private val wardrobeAdapter = WardrobeAdapter()

    fun setup(init: WardrobeViewHolder.() -> Unit): WardrobeViewHolder {
        init.invoke(this)

        with(wardrobeAdapter) {
            onCategoryClick = { categoryItem ->
                with(rootView.wardrobeToolbar) {
                    setNavigationIcon(R.drawable.ic_arrow_back)
                    title = categoryItem.name
                }
                this@WardrobeViewHolder.onCategoryClick?.invoke(categoryItem)
            }
            onThingSelected = { thingItem, position ->
                wardrobeAdapter.items[position] = thingItem
                wardrobeAdapter.notifyItemChanged(position)
                this@WardrobeViewHolder.onThingSelected?.invoke(thingItem)
            }
        }

        with(rootView) {
            wardrobeToolbar.setNavigationOnClickListener { onBackClick?.invoke() }
            wardrobeRecyclerView.adapter = wardrobeAdapter
        }

        return this
    }

    fun showCategories(categories: List<CategoryItem>) {
        wardrobeAdapter.showCategories(categories)
    }

    fun showThings(things: List<ThingItem>) {
        wardrobeAdapter.showThings(things)
    }

    fun showDefaultToolbar() {
        with(rootView.wardrobeToolbar) {
            setTitle(R.string.wardrobe)
            navigationIcon = null
        }
    }


}