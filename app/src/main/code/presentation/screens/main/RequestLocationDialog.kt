package presentation.screens.main

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.github.keyrillanskiy.cloather.R

/**
 * Диалог для объяснения причины запроса разрешения на местоположение
 *
 * @author Keyrillanskiy
 * @since 30.01.2019, 20:53.
 */
class RequestLocationDialog : DialogFragment() {

    var onPermitClick: (() -> Unit)? = null
    var onCancelClick: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setMessage(R.string.location_permission_reason)
            .setPositiveButton(R.string.permit) { _, _ -> onPermitClick?.invoke() }
            .setNegativeButton(R.string.cancel) { _, _ -> onCancelClick?.invoke() }
            .setOnCancelListener { onCancelClick?.invoke() }
            .create()
    }

    companion object {
        val TAG = RequestLocationDialog::class.java.simpleName
    }

}