package data.logging

import timber.log.Timber

/**
 * Логирование в debug сборке
 *
 * @author Keyrillanskiy
 * @since 12.01.2019, 17:06.
 */
class DebugLogTree : Timber.DebugTree() {

    override fun createStackElementTag(element: StackTraceElement): String? {
        return "${super.createStackElementTag(element)}, line: ${element.lineNumber}"
    }

}