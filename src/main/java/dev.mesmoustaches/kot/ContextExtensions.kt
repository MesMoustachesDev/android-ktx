package kot

import android.content.Context
import androidx.appcompat.app.AlertDialog

fun Context.displaySingleButtonAlertDialog(title: String? = null, message: String ?= null, action: (() -> Unit?)? = null) {
    val dialogBuilder = AlertDialog.Builder(this)
            message?.let {
                dialogBuilder.setMessage(message)

            }
    dialogBuilder.setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
                action?.invoke()
            }

    title?.let { nonNullTitle -> dialogBuilder.setTitle(nonNullTitle) }
    val dialog = dialogBuilder.create()
    dialog.show()
}

fun Context.displayCancelableAlertDialog(title: String? = null,
                                         message: String ?= null,
                                         cancelButton: Int? = null,
                                         okButton: Int? = null,
                                         action: (() -> Unit?)? = null) {
    val dialogBuilder = AlertDialog.Builder(this)
    message?.let {
        dialogBuilder.setMessage(message)

    }
    dialogBuilder.setPositiveButton(okButton ?: android.R.string.ok) { dialog, _ ->
        dialog.dismiss()
        action?.invoke()
    }

    dialogBuilder.setNegativeButton(cancelButton ?: android.R.string.cancel) { dialog, _ ->
        dialog.dismiss()
    }

    title?.let { nonNullTitle -> dialogBuilder.setTitle(nonNullTitle) }
    val dialog = dialogBuilder.create()
    dialog.show()
}