package presentation.screens.weather

import android.graphics.drawable.Drawable
import android.view.View
import domain.models.values.Gender
import extensions.disable
import extensions.enable
import kotlinx.android.synthetic.main.fragment_weather.view.*
import kotlinx.android.synthetic.main.item_weather_current.view.*
import presentation.common.BaseViewHolder

/**
 * @author Keyrillanskiy
 * @since 19.01.2019, 14:02.
 */
class WeatherViewHolder(private val rootView: View) : BaseViewHolder(rootView) {

    private val adapter = WeatherAdapter()

    var onRefresh: (() -> Unit)? = null

    fun setup(block: WeatherViewHolder.() -> Unit): WeatherViewHolder {
        block()

        rootView.run {
            weatherRecyclerView.adapter = adapter
            //todo убрать костыль с инициализацией itemViewHolder
            adapter.showCurrentWeather(WeatherCurrentItemData())
            mainRefreshLayout.setOnRefreshListener { onRefresh?.invoke() }
        }

        return this
    }

    fun showHuman(gender: Gender) {
        adapter.showHuman(gender)
    }

    fun showHumanPlaceholder(gender: Gender) {
        adapter.showHumanPlaceholder(gender)
    }

    fun showCurrentWeather(currentWeather: WeatherCurrentItemData) {
        adapter.showCurrentWeather(currentWeather)
    }

    fun showWeatherForecast(forecast: List<WeatherForecastItemData>) {
        adapter.showForecast(forecast)
    }

    fun showClothes(clothes: Drawable) {
        adapter.showClothes(clothes)
    }

    fun showRefreshing() {
        rootView.mainRefreshLayout.isRefreshing = true
    }

    fun hideRefreshing() {
        rootView.mainRefreshLayout.isRefreshing = false
    }

    fun enableRefreshing() = rootView.mainRefreshLayout.enable()

    fun disableRefreshing() = rootView.mainRefreshLayout.disable()

}