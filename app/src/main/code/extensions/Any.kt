package extensions

/**
 * @author Keyrillanskiy
 * @since 10/18/2018, 5:38 PM
 */

/**
 * Позволяет выполнить [block] с nullable параметрами, предварительно проверив, что они не равны null.
 * Иначе [block] не будет вызван.
 * Аналог вызова nullableValue?.let { value -> ... }, только для нескольких параметров.
 */
inline fun <T1 : Any, T2 : Any, R : Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}

/**
 * @see [safeLet] с 3-мя параметрами
 */
inline fun <T1 : Any, T2 : Any, T3 : Any, R : Any> safeLet(p1: T1?, p2: T2?, p3: T3?, block: (T1, T2, T3) -> R?): R? {
    return if (p1 != null && p2 != null && p3 != null) block(p1, p2, p3) else null
}

/**
 * @see [safeLet] с 4-мя параметрами
 */
inline fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, R : Any> safeLet(
    p1: T1?,
    p2: T2?,
    p3: T3?,
    p4: T4?,
    block: (T1, T2, T3, T4) -> R?
): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null) block(p1, p2, p3, p4) else null
}

/**
 * @see [safeLet] с 5-ю параметрами
 */
inline fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any, R : Any> safeLet(
    p1: T1?,
    p2: T2?,
    p3: T3?,
    p4: T4?,
    p5: T5?,
    block: (T1, T2, T3, T4, T5) -> R?
): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null && p5 != null) block(p1, p2, p3, p4, p5) else null
}

fun Any?.isNull() = this == null

fun Any?.isNotNull() = !this.isNull()