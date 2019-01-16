package presentation.share

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.github.keyrillanskiy.cloather.R

/**
 * Диалог загрузки.
 * Содержит заголовок, ProgressBar и кнопку отмены
 *
 * @author Keyrillanskiy
 * @since 16.01.2019, 10:03.
 */
class ProgressDialog : DialogFragment() {

    companion object {
        val TAG: String = ProgressDialog::class.java.simpleName
        private const val ARG_TITLE = "cloather.args.title"

        fun newInstance(title: String? = null) = ProgressDialog().apply {
            arguments = Bundle().apply { putString(ARG_TITLE, title) }
            isCancelable = false
        }
    }

    var onCancel: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val title = arguments?.getString(ARG_TITLE)

        return AlertDialog.Builder(requireContext()).apply {
            title?.let { setTitle(title) }
            onCancel?.let { lambda -> setPositiveButton(R.string.cancel) { _, _ -> lambda.invoke() } }
            setView(R.layout.dialog_progress)
        }.create()
    }

}