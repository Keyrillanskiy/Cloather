package presentation.screens.wardrobe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.keyrillanskiy.cloather.R
import extensions.loadImageFromBackend
import kotlinx.android.synthetic.main.item_wardrobe_category.view.*
import kotlinx.android.synthetic.main.item_wardrobe_thing.view.*

/**
 * Адаптер гардероба. Может отображать список категорий одежды или список вещей.
 *
 * @author Keyrillanskiy
 * @since 27.02.2019, 12:50.
 */
class WardrobeAdapter : RecyclerView.Adapter<WardrobeItemViewHolder>() {

    var onCategoryClick: ((categoryItem: CategoryItem) -> Unit)? = null
    var onThingSelected: ((thingItem: ThingItem, position: Int) -> Unit)? = null

    val items = mutableListOf<WardrobeItem>()

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is CategoryItem -> ITEM_CATEGORY
            is ThingItem -> ITEM_THING
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WardrobeItemViewHolder {
        val context = parent.context
        return when (viewType) {
            ITEM_CATEGORY -> {
                val itemView = LayoutInflater.from(context).inflate(R.layout.item_wardrobe_category, parent, false)
                CategoryItemViewHolder(itemView)
            }
            ITEM_THING -> {
                val itemView = LayoutInflater.from(context).inflate(R.layout.item_wardrobe_thing, parent, false)
                ThingItemViewHolder(itemView)
            }
            else -> throw IllegalArgumentException("unknown viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: WardrobeItemViewHolder, position: Int) {
        val item = items[position]
        when {
            holder is CategoryItemViewHolder && item is CategoryItem -> {
                holder.bindData(item)
                holder.onCategoryClick = { onCategoryClick?.invoke(item) }
            }
            holder is ThingItemViewHolder && item is ThingItem -> {
                holder.bindData(item)
                holder.onThingSelected = { newItem -> onThingSelected?.invoke(newItem, position) }
            }
            else -> throw IllegalArgumentException("illegal combination of holder and item: $holder + $item")
        }
    }

    override fun getItemCount(): Int = items.size

    fun showCategories(categories: List<CategoryItem>) {
        with(items) {
            clear()
            addAll(categories)
        }
        notifyDataSetChanged()
    }

    fun showThings(things: List<ThingItem>) {
        with(items) {
            clear()
            addAll(things)
        }
        notifyDataSetChanged()
    }

    companion object {
        private const val ITEM_CATEGORY = 1
        private const val ITEM_THING = 2
    }

}

sealed class WardrobeItemViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView)

class CategoryItemViewHolder(private val rootView: View) : WardrobeItemViewHolder(rootView) {

    var onCategoryClick: ((categoryItem: CategoryItem) -> Unit)? = null

    fun bindData(data: CategoryItem) {
        with(rootView) {
            wardrobeCategoryNameTextView.text = data.name
            setOnClickListener { onCategoryClick?.invoke(data) }
            loadImageFromBackend(context, data.imageUrl, wardrobeCategoryImageView)
        }
    }

}

class ThingItemViewHolder(private val rootView: View) : WardrobeItemViewHolder(rootView) {

    var onThingSelected: ((thingItem: ThingItem) -> Unit)? = null

    fun bindData(data: ThingItem) {
        with(rootView) {
            wardrobeThingNameTextView.text = data.name
            if(data.isChecked) {
                thingCheckIndicator.setImageResource(R.drawable.ic_checked_circle)
            } else {
                thingCheckIndicator.setImageResource(R.drawable.ic_unckecked_circle)
            }
            setOnClickListener {
                val newData = data.copy(isChecked = !data.isChecked)
                onThingSelected?.invoke(newData)
            }
            loadImageFromBackend(context, data.imageUrl, wardrobeThingImageView)
        }
    }

}

sealed class WardrobeItem

data class CategoryItem(
    var id: String,
    var name: String,
    var imageUrl: String?
) : WardrobeItem()

data class ThingItem(
    var id: String,
    var name: String,
    var imageUrl: String?,
    var isChecked: Boolean
) : WardrobeItem()
