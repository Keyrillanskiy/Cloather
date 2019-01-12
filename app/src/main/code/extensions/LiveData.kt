package extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

/**
 * @author Keyrillanskiy
 * @since 10/18/2018, 5:49 PM
 */

/**
 * Функцию требуется использовать для подписки на [LiveData] в фрагменте.
 * Это связано с особенностями жизненного цикла фрагмента.
 * Подробнее: https://medium.com/@BladeCoder/architecture-components-pitfalls-part-1-9300dd969808
 */
fun <T> LiveData<T>.reObserve(owner: LifecycleOwner, observer: Observer<T>) {
    removeObserver(observer)
    observe(owner, observer)
}

/**
 * Функция позволяет объединить данные из двух [LiveData] в одну
 *
 * @author Pavel Annin (https://github.com/anninpavel).
 */
fun <T1, T2> combineLiveData(t1: LiveData<T1>, t2: LiveData<T2>): LiveData<Pair<T1, T2>> {
    return MediatorLiveData<Pair<T1, T2>>().apply {

        var lastT1: T1? = null
        var lastT2: T2? = null

        fun update(localLastT1: T1? = lastT1, localLastT2: T2? = lastT2) {
            if (localLastT1 != null && localLastT2 != null) {
                value = Pair(localLastT1, localLastT2)
            }
        }

        addSource(t1) {
            lastT1 = it
            update()
        }

        addSource(t2) {
            lastT2 = it
            update()
        }
    }
}