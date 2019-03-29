package presentation.screens.weather

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.github.keyrillanskiy.cloather.R
import domain.models.values.Gender
import domain.models.values.Language
import domain.models.values.WeatherType
import extensions.visible
import kotlinx.android.synthetic.main.item_weather_current.view.*
import kotlinx.android.synthetic.main.item_weather_forecast.view.*

/**
 * @author Keyrillanskiy
 * @since 25.03.2019, 18:00.
 */
class WeatherAdapter : RecyclerView.Adapter<WeatherItemViewHolder>() {

    private val items = mutableListOf<WeatherItemData>()

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            CURRENT_WEATHER_ITEM_POSITION -> CURRENT_WEATHER_ITEM
            else -> FORECAST_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            CURRENT_WEATHER_ITEM -> {
                val itemView = inflater.inflate(R.layout.item_weather_current, parent, false)
                WeatherCurrentItemViewHolder(itemView)
            }
            FORECAST_ITEM -> {
                val itemView = inflater.inflate(R.layout.item_weather_forecast, parent, false)
                WeatherForecastItemViewHolder(itemView)
            }
            else -> throw IllegalArgumentException("unknown item viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: WeatherItemViewHolder, position: Int) {
        val item = items[position]
        when {
            holder is WeatherCurrentItemViewHolder && item is WeatherCurrentItemData -> holder.bindData(item)
            holder is WeatherForecastItemViewHolder && item is WeatherForecastItemData -> holder.bindData(item)
        }
    }

    override fun getItemCount(): Int = items.size

    fun showCurrentWeather(currentWeather: WeatherCurrentItemData) {
        if (items.size == 0) {
            items.add(currentWeather)
        } else {
            items[CURRENT_WEATHER_ITEM_POSITION] = currentWeather
        }
        notifyItemChanged(CURRENT_WEATHER_ITEM_POSITION)
    }

    fun showForecast(forecast: List<WeatherForecastItemData>) {
        items.removeAll { it is WeatherForecastItemData }
        items.addAll(forecast)
        notifyDataSetChanged()
    }

    fun showHuman(gender: Gender) {
        val item = items[CURRENT_WEATHER_ITEM_POSITION] as? WeatherCurrentItemData
            ?: throw IllegalStateException("illegal item data: ${items[CURRENT_WEATHER_ITEM_POSITION]}")

        val humanResId = when (gender) {
            Gender.MALE -> R.drawable.ic_man
            Gender.FEMALE -> R.drawable.ic_man //todo woman
            Gender.UNDEFINED -> throw IllegalArgumentException("illegal gender: $gender")
        }
        val newItem = item.copy(humanImageResource = humanResId)
        items[CURRENT_WEATHER_ITEM_POSITION] = newItem
        notifyItemChanged(CURRENT_WEATHER_ITEM_POSITION)
    }

    fun showHumanPlaceholder(gender: Gender) {
        val item = items[CURRENT_WEATHER_ITEM_POSITION] as? WeatherCurrentItemData
            ?: throw IllegalStateException("illegal item data: ${items[CURRENT_WEATHER_ITEM_POSITION]}")

        val humanResId = when (gender) {
            Gender.MALE -> R.drawable.ic_man_placeholder
            Gender.FEMALE -> R.drawable.ic_man_placeholder //todo woman
            Gender.UNDEFINED -> throw IllegalArgumentException("illegal gender: $gender")
        }
        val newItem = item.copy(humanImageResource = humanResId, clothesDrawable = null)
        items[CURRENT_WEATHER_ITEM_POSITION] = newItem
        notifyItemChanged(CURRENT_WEATHER_ITEM_POSITION)
    }

    fun showClothes(drawable: Drawable) {
        val item = items[CURRENT_WEATHER_ITEM_POSITION] as? WeatherCurrentItemData
            ?: throw IllegalStateException("illegal item data: ${items[CURRENT_WEATHER_ITEM_POSITION]}")

        val newItem = item.copy(clothesDrawable = drawable)
        items[CURRENT_WEATHER_ITEM_POSITION] = newItem
        notifyItemChanged(CURRENT_WEATHER_ITEM_POSITION)
    }

    private companion object {
        private const val CURRENT_WEATHER_ITEM = 1
        private const val FORECAST_ITEM = 2

        private const val CURRENT_WEATHER_ITEM_POSITION = 0
    }

}

sealed class WeatherItemViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView)

class WeatherCurrentItemViewHolder(private val rootView: View) : WeatherItemViewHolder(rootView) {

    fun bindData(data: WeatherCurrentItemData) {
        with(rootView) {
            weatherLocationTextView.text = data.city ?: ""
            weatherDegreesTextView.text = data.temperature ?: ""
            when (data.currentLanguage) {
                Language.RUSSIAN -> weatherTypeTextView.text = data.type?.inRussian ?: ""
                Language.ENGLISH -> weatherTypeTextView.text = data.type?.inEnglish ?: ""
            }
            data.humanImageResource?.let {
                weatherHumanImageView.setImageResource(it)
                weatherHumanImageView.visible()
            }
            weatherClothesImageView.setImageDrawable(data.clothesDrawable)
        }
    }

}

class WeatherForecastItemViewHolder(private val rootView: View) : WeatherItemViewHolder(rootView) {

    fun bindData(data: WeatherForecastItemData) {
        with(rootView) {
            weatherForecastTimeTextView.text = data.time
            weatherForecastDegreesTextView.text = data.temperature
            weatherForecastImageView.setImageResource(data.type.drawableRes)
        }
    }

}

sealed class WeatherItemData

data class WeatherCurrentItemData(
    val city: String? = null,
    val temperature: String? = null,
    val type: WeatherType? = null,
    val currentLanguage: Language? = null,
    @DrawableRes val humanImageResource: Int? = null,
    val clothesDrawable: Drawable? = null
) : WeatherItemData()

data class WeatherForecastItemData(
    val time: String,
    val type: WeatherType,
    val temperature: String
) : WeatherItemData()
