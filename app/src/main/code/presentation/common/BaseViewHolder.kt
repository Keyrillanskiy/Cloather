package presentation.common

import android.content.Context
import android.content.res.Resources
import android.view.View

/**
 * Базовый ViewHolder
 *
 * @author Keyrillanskiy
 * @since 19.01.2019, 14:04.
 */
abstract class BaseViewHolder(private val rootView: View) {

    protected val context: Context
        get() = rootView.context

    protected val resource: Resources
        get() = rootView.resources

}