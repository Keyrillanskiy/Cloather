package presentation.screens.weather

import android.view.View
import android.view.ViewGroup
import com.crashlytics.android.Crashlytics
import com.github.keyrillanskiy.cloather.BuildConfig
import com.github.keyrillanskiy.cloather.R
import extensions.disable
import extensions.enable
import extensions.visible
import kotlinx.android.synthetic.main.fragment_weather.view.*
import presentation.common.BaseViewHolder

/**
 * @author Keyrillanskiy
 * @since 19.01.2019, 14:02.
 */
class WeatherViewHolder(private val rootView: View) : BaseViewHolder(rootView) {

    var onRefresh: (() -> Unit)? = null

    fun setup(block: WeatherViewHolder.() -> Unit): WeatherViewHolder {
        block()

        rootView.run {
            mainRefreshLayout.setOnRefreshListener { onRefresh?.invoke() }
        }

        return this
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