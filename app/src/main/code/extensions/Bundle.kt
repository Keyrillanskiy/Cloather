package extensions

import android.os.Bundle

/**
 * Extensions для [android.os.Bundle].
 *
 * @author Keyrillanskiy
 * @since 10/18/2018, 5:40 PM
 */

/**
 * Возвращает значение по ключу из bundle. Если bundle не содержит значения по указанному ключу,
 * то функция выбросит исключение [IllegalStateException]
 *
 *  @param key Ключ
 *
 *  @return Значение по ключу
 */
@Suppress(names = ["UNCHECKED_CAST"])
fun <T> Bundle.required(key: String): T {
    check(value = containsKey(key), lazyMessage = { "Value with key $key not found." })
    return get(key) as T
}

/**
 * Возвращает значение по ключу из bundle. Если bundle не содержит значения по указанному ключу,
 * то функция выбросит исключение [IllegalArgumentException].
 *
 *  @param key Ключ
 *  @param alternative Альтернативный bundle, в котором функция попробует найти данные, если
 *  в указанном bundle их нет
 *
 *  @return Значение по ключу
 */
@Suppress(names = ["UNCHECKED_CAST"])
fun <T> Bundle?.required(key: String, alternative: Bundle? = null): T {
    return when {
        this?.containsKey(key) ?: false -> requireNotNull(value = this).required(key)
        alternative?.containsKey(key) ?: false -> requireNotNull(value = alternative).required(key)
        else -> throw IllegalArgumentException("Value with key $key not found.")
    }
}