package application

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.github.keyrillanskiy.cloather.BuildConfig
import com.squareup.leakcanary.LeakCanary
import data.logging.DebugLogTree
import data.logging.ReleaseLogTree
import di.*
import org.koin.android.ext.android.startKoin
import timber.log.Timber
import utils.NetUtils

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
        setupDependencyInjection()
        NetUtils.init(this)
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

    private fun setupDependencyInjection() {
        val modules = listOf(
            commonModule, repositoriesModule, useCasesModule, apisModule,
            authModule, genderModule, mainModule
        )
        startKoin(this, modules)
    }

}