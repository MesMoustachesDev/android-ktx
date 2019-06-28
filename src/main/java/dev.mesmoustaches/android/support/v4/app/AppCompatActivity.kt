package android.support.v4.app

import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.casino.fidelite.uicore.R

fun AppCompatActivity.hideKeyBoard() {
    val viewFocused = currentFocus ?: View(this)
    val imm = getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(viewFocused.windowToken, 0)
}

fun AppCompatActivity.exitModalWay() {
    overridePendingTransition(R.anim.nothing, R.anim.slide_out_bottom)
}

fun Fragment.hideKeyBoard() {
    val viewFocused = activity?.currentFocus ?: View(requireContext())
    val imm = activity?.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(viewFocused.windowToken, 0)
}

fun AppCompatActivity.addNotificationHeightPadding(view: View) {
    view.setPadding(0, getStatusBarHeight(), 0, 0)
}

fun AppCompatActivity.getStatusBarHeight(): Int {
    // A method to find height of the status bar
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId)
    }
    return result
}