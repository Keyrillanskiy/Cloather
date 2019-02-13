package presentation.screens.main

import android.view.ViewGroup
import com.crashlytics.android.Crashlytics
import com.github.keyrillanskiy.cloather.BuildConfig
import com.github.keyrillanskiy.cloather.R
import extensions.disable
import extensions.enable
import extensions.visible
import kotlinx.android.synthetic.main.activity_main.view.*
import presentation.common.BaseViewHolder

/**
 * @author Keyrillanskiy
 * @since 19.01.2019, 14:02.
 */
class MainViewHolder(private val rootView: ViewGroup) : BaseViewHolder(rootView) {

    var onRefresh: (() -> Unit)? = null
    var onWardrobeClick: (() -> Unit)? = null
    var onSettingsClick: (() -> Unit)? = null

    fun setup(block: MainViewHolder.() -> Unit): MainViewHolder {
        block()

        rootView.run {
            mainToolbar.inflateMenu(R.menu.menu_main)

            mainToolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_wardrobe -> onWardrobeClick?.invoke()
                    R.id.action_settings -> onSettingsClick?.invoke()
                }
                true
            }

            mainRefreshLayout.setOnRefreshListener { onRefresh?.invoke() }

            if (BuildConfig.BUILD_TYPE == "debugCrashes") {
                crashButton.visible()
                crashButton.setOnClickListener { Crashlytics.getInstance().crash() }
            }
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