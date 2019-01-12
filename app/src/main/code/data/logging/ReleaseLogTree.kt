package data.logging

import android.util.Log
import timber.log.Timber

/**
 * Логирование в release сборке
 *
 * @author Keyrillanskiy
 * @since 12.01.2019, 17:07.
 */
class ReleaseLogTree : Timber.Tree() {

    /**
     * Описание приоритетов логов в release сборке приложения:
     *
     * DEBUG - данные для отладки, которые должны быть видны только разработчику
     * INFO - данные, которые будут видны в статистике. Они дают контекст перед тем, как произойдет ошибка с приоритетом WARNING, ASSERT или ERROR
     * WARNING и ASSERT - в приложении произошла ошибка, но она не является критической и система может восстановиться сама (например, данные не загрузились из-за отсутствия интернета)
     * ERROR - в приложении произошла критическая ошибка, которую нужно максимально быстро исправить
     */

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.ERROR || priority == Log.WARN || priority == Log.ASSERT) {
            //TODO логировать сообщение и исключение (после добавления системы crash reporting'а)
        } else if (priority == Log.INFO) {
            //TODO логировать сообщение (после добавления системы crash reporting'а)
        }
    }

}