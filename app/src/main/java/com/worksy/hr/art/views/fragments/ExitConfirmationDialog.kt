import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class ExitConfirmationDialog : DialogFragment() {
    var onExitConfirmed: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes") { _, _ -> onExitConfirmed?.invoke() }
            .setNegativeButton("No", null)
            .create()
    }
}
