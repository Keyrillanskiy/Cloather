package application

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.github.keyrillanskiy.cloather.BuildConfig
import com.squareup.leakcanary.LeakCanary
import data.logging.DebugLogTree
import data.logging.ReleaseLogTree
import timber.log.Timber

/**
 * @author Keyrillanskiy
 * @since 12.01.2019, 16:50.
 */
@Suppress("unused")
class CloatherApplication : MultiDexApplication() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        setupTimber()
        setupLeakCanary()
    }

    private fun setupTimber() {
        val logTree = if (BuildConfig.DEBUG) DebugLogTree() else ReleaseLogTree()
        Timber.plant(logTree)
    }

    private fun setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)
    }

}