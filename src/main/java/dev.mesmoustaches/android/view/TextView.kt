package android.view

import android.graphics.Paint
import android.graphics.PorterDuff
import android.os.Build
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.View.OnTouchListener
import android.widget.TextView



private fun makeLinkClickable(strBuilder: SpannableStringBuilder, span: URLSpan, listener: ((String) -> Unit)? = null) {
    val start = strBuilder.getSpanStart(span)
    val end = strBuilder.getSpanEnd(span)
    val flags = strBuilder.getSpanFlags(span)
    val clickable = object : ClickableSpan() {
        override fun onClick(view: View?) {
            listener?.invoke(span.url)
        }
    }
    strBuilder.setSpan(clickable, start, end, flags)
    strBuilder.removeSpan(span)
}

fun TextView.setTextViewHTML(html: String, listener: ((String) -> Unit)? = null) {
    val sequence = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(html)
    }
    val strBuilder = SpannableStringBuilder(sequence)
    val urls = strBuilder.getSpans(0, sequence.length, URLSpan::class.java)
    urls.forEach {
        makeLinkClickable(strBuilder, it, listener)
    }
    text = strBuilder
    movementMethod = LinkMovementMethod.getInstance()
}

fun TextView.setOnRightDrawableClickedListener(listener: () -> Unit) {
    setOnTouchListener(OnTouchListener { v, event ->
        if (event.action == MotionEvent.ACTION_UP) {
            if(event.rawX >= right - totalPaddingRight) {
                listener.invoke()
                return@OnTouchListener true
            }
        }
        false
    })
}

fun TextView.setOnLeftDrawableClickedListener(listener: () -> Unit) {
    setOnTouchListener(OnTouchListener { v, event ->
        if (event.action == MotionEvent.ACTION_UP) {
            if(event.rawX <= totalPaddingLeft) {
                // your action for drawable click event
                listener.invoke()
                return@OnTouchListener true
            }
        }
        false
    })
}

fun TextView.tintStartDrawable(color: Int) {
    val drawables = compoundDrawables
    if (drawables[0] != null) {  // left drawable
        drawables[0].setColorFilter(color, PorterDuff.Mode.MULTIPLY)
    }
}


fun TextView.underline() {
    paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
}