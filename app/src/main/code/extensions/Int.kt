package extensions

import android.content.res.Resources

/**
 * @author Keyrillanskiy
 * @since 10/18/2018, 5:45 PM
 */

/**
 * Перевод из Px в Dp
 */
val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

/**
 *  Перевод из Dp в Px
 */
val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()