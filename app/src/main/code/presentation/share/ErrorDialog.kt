package presentation.share

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.github.keyrillanskiy.cloather.R

/**
 * Диалог ошибки с кнопками "Ок" и "Повторить"
 *
 * @author Keyrillanskiy
 * @since 16.01.2019, 20:45.
 */
class ErrorDialog : DialogFragment() {

    var onOkClick: (() -> Unit)? = null
    var onRetryClick: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val title = arguments?.getString(EXTRA_TITLE)
        val message = arguments?.getString(EXTRA_MESSAGE)

        return AlertDialog.Builder(requireContext()).apply {
            title?.let { setTitle(it) }
            message?.let { setMessage(it) }

            setPositiveButton(R.string.ok) { _, _ -> onOkClick?.invoke() }
            onRetryClick?.let { click ->
                setNegativeButton(R.string.retry) { _, _ -> click.invoke() }
            }
        }.create()

    }

    companion object {
        val TAG = ErrorDialog::class.java.simpleName
        private const val EXTRA_TITLE = "cloather.args.title"
        private const val EXTRA_MESSAGE = "cloather.args.message"

        fun newInstance(title: String? = null, message: String? = null) = ErrorDialog().apply {
            arguments = Bundle().apply {
                putString(EXTRA_TITLE, title)
                putString(EXTRA_MESSAGE, message)
            }
        }
    }

}