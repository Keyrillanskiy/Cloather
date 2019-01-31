package utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * @author Keyrillanskiy
 * @since 30.01.2019, 21:30.
 */
object PermissionUtils {

    private val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .ACCESS_COARSE_LOCATION
    )

    private fun isPermissionGranted(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun isPermissionsGranted(context: Context, permissions: Array<String>): Boolean {
        return permissions.all { ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED }
    }

    fun isLocationPermissionGranted(context: Context): Boolean {
        return isPermissionsGranted(context, locationPermissions)
    }

    fun requestLocationPermission(activity: Activity, requestCode: Int) {
        ActivityCompat.requestPermissions(activity, locationPermissions, requestCode)
    }

}