package android.utils

import android.content.res.Resources
import android.util.TypedValue

class DimensionsUtils {
    companion object {
        fun getDpFromPx(pxValue: Int): Int {
            val metrics = Resources.getSystem().displayMetrics
            val dp = pxValue / (metrics.densityDpi / 160f)
            return Math.round(dp)
        }

        fun getPxFromDp(dpValue: Int): Int {
            val metrics = Resources.getSystem().displayMetrics
            return TypedValue
                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue.toFloat(), metrics)
                    .toInt()
        }
    }
}