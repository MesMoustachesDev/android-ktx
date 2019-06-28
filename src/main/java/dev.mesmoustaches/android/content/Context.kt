package android.content

import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.Gravity
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.casino.fidelite.uicore.R
import com.google.android.material.snackbar.Snackbar
import support.design.widget.setTextColor

fun Context.showSnackBar(resText: Int, mainRoot: View, duration: Int = Snackbar.LENGTH_LONG, backgroundColor: Int? = R.color.brown_grey, textColor: Int? = R.color.textColor, anchor: Int? = null) {
    showSnackBar(getString(resText), mainRoot, duration, backgroundColor, textColor, anchor)
}

fun Context.showSnackBar(message: String,
                         mainRoot: View,
                         duration: Int = Snackbar.LENGTH_LONG,
                         backgroundColor: Int? = R.color.brown_grey,
                         textColor: Int? = R.color.textColor,
                         anchor: Int? = null,
                         argGravity: Int = Gravity.TOP) {
    val spanString = SpannableString(message)
    spanString.setSpan(StyleSpan(Typeface.BOLD), 0, spanString.length, 0)

    val snackBar = Snackbar.make(mainRoot, spanString, duration)

    snackBar.view.layoutParams = (snackBar.view.layoutParams as? CoordinatorLayout.LayoutParams)?.apply {
        anchor?.let {
            anchorId = it //Id for your bottomNavBar or TabLayout
            anchorGravity = argGravity
            gravity = argGravity
        }
    } ?: snackBar.view.layoutParams

    textColor?.let { snackBar.setTextColor(textColor) }
    backgroundColor?.let { snackBar.view.setBackgroundColor(ContextCompat.getColor(this, backgroundColor)) }
    ViewCompat.setElevation(snackBar.view, 0f)
    snackBar.show()
}

