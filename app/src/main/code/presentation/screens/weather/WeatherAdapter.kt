package presentation.screens.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.keyrillanskiy.cloather.R
import domain.models.values.Language
import domain.models.values.WeatherType
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
            weatherLocationTextView.text = data.city
            weatherDegreesTextView.text = data.temperature
            when (data.currentLanguage) {
                Language.RUSSIAN -> weatherTypeTextView.text = data.type.inRussian
                Language.ENGLISH -> weatherTypeTextView.text = data.type.inEnglish
            }
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
    val city: String,
    val temperature: String,
    val type: WeatherType,
    val currentLanguage: Language
) : WeatherItemData()

data class WeatherForecastItemData(
    val time: String,
    val type: WeatherType,
    val temperature: String
) : WeatherItemData()
