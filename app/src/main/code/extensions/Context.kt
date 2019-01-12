package extensions

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * @author Keyrillanskiy
 * @since 10/18/2018, 5:45 PM
 */

/**
 * Показывает короткий toast (LENGTH_SHORT)
 *
 * @param message Выводимое сообщение
 */
fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

/**
 * @see [toast]
 *
 * @param stringResId analyseId Выводимого строкового ресурса
 */
fun Context.toast(@StringRes stringResId: Int) =
    Toast.makeText(this, stringResId, Toast.LENGTH_SHORT).show()