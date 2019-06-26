package hbs.com.snackb.utils

import android.app.Activity
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar


class SnackbarHelper(val activity: Activity) {
    private val BACKGROUND_COLOR = 0xbf323232
    private var messageSnackbar: Snackbar? = null

    private enum class DismissBehavior {
        HIDE, SHOW, FINISH
    };
    fun isShowing(): Boolean {
        return messageSnackbar != null
    }

    /** Shows a snackbar with a given message.  */
    fun showMessage(message: String) {
        show(message, DismissBehavior.HIDE)
    }

    /** Shows a snackbar with a given message, and a dismiss button.  */
    fun showMessageWithDismiss(message: String) {
        show(message, DismissBehavior.SHOW)
    }

    fun showError(errorMessage: String) {
        show(errorMessage, DismissBehavior.FINISH)
    }

    fun hide(activity: Activity) {
        activity.runOnUiThread {
            if (messageSnackbar != null) {
                messageSnackbar?.dismiss()
            }
            messageSnackbar = null
        }
    }

    private fun show(message: String, dismissBehavior: DismissBehavior) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}