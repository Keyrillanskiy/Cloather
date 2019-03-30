package utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest

/**
 * @author Keyrillanskiy
 * @since 16.01.2019, 10:25.
 */
object NetUtils {

    lateinit var connectivityManager: ConnectivityManager

    fun init(context: Context) {
        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    fun withNetConnection(
        onSuccess: (() -> Unit)? = null,
        onError: (() -> Unit)? = null,
        onConnected: (() -> Unit)? = null
    ) {
        if (hasNetConnection()) {
            onSuccess?.invoke()
        } else {
            onError?.invoke()

            onConnected?.let { callback ->
                registerNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network?) {
                        connectivityManager.unregisterNetworkCallback(this)

                        callback.invoke()
                    }
                })
            }
        }
    }

    fun hasNetConnection(): Boolean {
        val netInfo = connectivityManager.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }

    private fun registerNetworkCallback(callback: ConnectivityManager.NetworkCallback) {
        val request = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(request, callback)
    }

}