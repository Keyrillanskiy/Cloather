package application

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.github.keyrillanskiy.cloather.BuildConfig
import com.squareup.leakcanary.LeakCanary
import data.logging.DebugLogTree
import data.logging.ReleaseLogTree
import di.*
import io.fabric.sdk.android.Fabric
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

        setupCrashReporting()
        setupTimber()
        setupLeakCanary()
        setupDependencyInjection()
        NetUtils.init(this)
    }

    private fun setupCrashReporting() {
        val isDisabled = !(BuildConfig.BUILD_TYPE == "release" || BuildConfig.BUILD_TYPE == "debugCrashes")
        val crashlytics = Crashlytics.Builder()
            .core(
                CrashlyticsCore.Builder()
                    .disabled(isDisabled)
                    .build()
            )
            .build()
        Fabric.with(this, crashlytics)
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
            authModule, genderModule, mainModule, settingsModule
        )
        startKoin(this, modules)
    }

}