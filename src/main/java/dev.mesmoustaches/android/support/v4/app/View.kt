package android.support.v4.app

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.core.widget.NestedScrollView

fun View.expand() {
    expand(-1, null, null, null)
}

fun View.expand(duration: Long) {
    expand(duration, null, null, null)
}

fun View.expand(listener: (() -> Unit)?) {
    expand(-1, null, null, listener)
}

fun View.collapse() {
    collapse(-1, null)
}

fun View.collapse(listener: (() -> Unit)?) {
    collapse(-1, listener)
}

fun View.collapse(duration: Long) {
    collapse(duration, null)
}

fun View.expand(duration: Long, scrollView: NestedScrollView?, expandingView: View?, listener: (() -> Unit)? = null) {
    val v = this
    v.measure(ViewGroup.LayoutParams.MATCH_PARENT, 0)
    val targetHeight = v.measuredHeight

    // Older versions of android (pre API 21) cancel animations for views with a height of 0.
    v.layoutParams.height = 1
    v.visibility = View.VISIBLE
    val a = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            v.layoutParams.height = if (interpolatedTime == 1f)
                ViewGroup.LayoutParams.WRAP_CONTENT
            else
                (targetHeight * interpolatedTime).toInt()
            v.requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    // 1dp/ms
    if (duration < 0) {
        a.duration = 300//(targetHeight / v.context.resources.displayMetrics.density).toLong()
    } else {
        a.duration = duration
    }
    v.startAnimation(a)
    if (scrollView != null && expandingView != null) {
        a.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation?) {
                //postDelayed({ scrollView.smoothScrollTo(0, top) }, 200)
                post(listener)
            }

            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        }
        )
    }
}

fun View.collapse(duration: Long, listener: (() -> Unit)?) {
    val v = this
    val initialHeight = v.measuredHeight

    val a = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            if (interpolatedTime == 1f) {
                v.visibility = View.GONE
            } else {
                v.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                v.requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    if (duration < 0) {
        a.duration = 300//(targetHeight / v.context.resources.displayMetrics.density).toLong()
    } else {
        a.duration = duration
    }
    v.startAnimation(a)
    a.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationEnd(animation: Animation?) {
            listener?.invoke()
        }

        override fun onAnimationStart(animation: Animation?) {
        }

        override fun onAnimationRepeat(animation: Animation?) {
        }
    })
}

fun View?.show(b: Boolean?) {
    this?.visibility = if (b == true) View.VISIBLE else View.GONE
}

val View.isVisible: Boolean
    get() = visibility == View.VISIBLE