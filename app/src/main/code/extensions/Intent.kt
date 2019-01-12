package extensions

import android.content.Intent
import android.content.pm.PackageManager

/**
 * @author Keyrillanskiy
 * @since 10/18/2018, 5:46 PM
 */

/**
 * Проверка является ли намерение безопасным (т.е. есть ли приложения которые его обработают).
 *
 * @param packageManager [PackageManager]
 * @param block Лямбда, которая выполнится, если намерение является безопасным
 */
inline fun <R> Intent.safe(packageManager: PackageManager, block: (Intent) -> R): R? {
    val activities = packageManager.queryIntentActivities(this, 0)
    return if ((activities?.size ?: 0) > 0) block.invoke(this) else null
}