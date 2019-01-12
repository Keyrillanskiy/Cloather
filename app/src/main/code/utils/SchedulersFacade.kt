package utils

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Фасад, который инкапсулирует работу с потоками в rxjava
 *
 * @author Keyrillanskiy
 * @since 12.01.2019, 17:09.
 */
class SchedulersFacade {
    val io: Scheduler
        get() = Schedulers.io()

    val computation: Scheduler
        get() = Schedulers.computation()

    val ui: Scheduler
        get() = AndroidSchedulers.mainThread()
}