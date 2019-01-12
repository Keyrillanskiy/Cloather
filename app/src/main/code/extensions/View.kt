package extensions

import android.view.View

/**
 * @author Keyrillanskiy
 * @since 10/18/2018, 5:53 PM
 */

/**
 * Вызывает [View.setVisibility] с параметром [View.VISIBLE].
 */
fun View.visible() {
    this.visibility = View.VISIBLE
}

/**
 * Вызывает [View.setVisibility] с параметром [View.INVISIBLE].
 */
fun View.invisible() {
    this.visibility = View.INVISIBLE
}

/**
 * Вызывает [View.setVisibility] с параметром [View.GONE].
 */
fun View.gone() {
    this.visibility = View.GONE
}

/**
 * Вызывает [View.isEnabled] с параметром true.
 */
fun View.enable() {
    this.isEnabled = true
}

/**
 * Вызывает [View.isEnabled] с параметром false.
 */
fun View.disable() {
    this.isEnabled = false
}

