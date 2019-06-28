package support.design.widget

import android.widget.TextView
import androidx.core.content.ContextCompat
import com.casino.fidelite.uicore.R
import com.google.android.material.snackbar.Snackbar

fun Snackbar.setTextColor(color: Int): Snackbar {
    val tv = view.findViewById(R.id.snackbar_text) as TextView
    tv.setTextColor(ContextCompat.getColor(context, color))

    return this
}