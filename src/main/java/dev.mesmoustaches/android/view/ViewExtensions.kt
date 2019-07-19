/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.view

import android.content.Context
import android.graphics.PorterDuff
import android.view.animation.Animation
import android.view.animation.Transformation
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.nonNullObserve

fun View.showOrGone(show: Boolean) {
    if (show) this.visibility = View.VISIBLE else this.visibility = View.GONE
}

fun View.showOrInvisible(show: Boolean) {
    if (show) this.visibility = View.VISIBLE else this.visibility = View.INVISIBLE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.isViewVisible(): Boolean = this.visibility == View.VISIBLE

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun Button.tintStartDrawable(color: Int) {
    val drawables = compoundDrawables
    if (drawables[0] != null) {  // left d
        drawables[0]// rawable
        drawables[0].setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    } else {
        val drawablesRelative = compoundDrawablesRelative
        if (drawablesRelative[0] != null) {  // left d
            drawablesRelative[0]// rawable
            drawablesRelative[0].setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }
}

fun View.setVisible(state: Boolean) {
    visibility = if (state) View.VISIBLE else View.GONE
}

fun View.setVisibleInvisible(state: Boolean) {
    visibility = if (state) View.VISIBLE else View.INVISIBLE
}

fun View.linkVisibilityTo(liveData: LiveData<Boolean>, lifecycleOwner: LifecycleOwner) {
    liveData.nonNullObserve(lifecycleOwner) {
        setVisible(it)
    }
}

fun View.linkVisibleInvisibleTo(liveData: LiveData<Boolean>, lifecycleOwner: LifecycleOwner) {
    liveData.nonNullObserve(lifecycleOwner) {
        setVisibleInvisible(it)
    }
}

fun View.linkEnabilityTo(liveData: LiveData<Boolean>, lifecycleOwner: LifecycleOwner) {
    liveData.nonNullObserve(lifecycleOwner) {
        isEnabled = it
    }
}

fun <T> View.linkVisibilityTo(liveData: LiveData<T>, lifecycleOwner: LifecycleOwner, transform: ((T) -> Boolean)) {
    liveData.nonNullObserve(lifecycleOwner) {
        setVisible(transform(it))
    }
}

fun View.closeKeyboard() {
    context?.let {
        (it.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(windowToken, 0)
    }
}

fun View.setWidth(newWidth: Int) {
    val params = this.layoutParams
    params.width = newWidth
    layoutParams = params
}

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