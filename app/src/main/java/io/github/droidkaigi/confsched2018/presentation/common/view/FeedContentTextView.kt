package io.github.droidkaigi.confsched2018.presentation.common.view

import android.content.Context
import android.support.text.emoji.widget.EmojiAppCompatTextView
import android.text.Selection
import android.text.Spannable
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.util.AttributeSet
import android.view.MotionEvent

class FeedContentTextView
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyleAttr: Int = 0
) : EmojiAppCompatTextView(context, attrs, defStyleAttr) {

    var onClickUrl: ((String) -> Unit)? = null

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action

        if (MotionEvent.ACTION_UP == action || MotionEvent.ACTION_DOWN == action) {

            if (text !is Spannable) return false

            val spannable = text as Spannable

            var x = event.x.toInt()
            var y = event.y.toInt()

            x -= totalPaddingStart
            y -= totalPaddingTop
            x += scrollX
            y += scrollY

            val line = layout.getLineForVertical(y)
            val offset = layout.getOffsetForHorizontal(line, x.toFloat())
            val urls = spannable.getSpans(offset, offset, ClickableSpan::class.java)

            return if (urls.isNotEmpty()) {
                val url: ClickableSpan = urls[0]

                when (action) {
                    MotionEvent.ACTION_UP -> {
                        if (url is URLSpan && onClickUrl != null) {
                            onClickUrl?.invoke(url.url)
                        } else {
                            url.onClick(this)
                        }
                    }
                    MotionEvent.ACTION_DOWN -> {
                        Selection.setSelection(spannable,
                                spannable.getSpanStart(url),
                                spannable.getSpanEnd(url))
                    }
                }
                true
            } else {
                Selection.removeSelection(spannable)
                false
            }
        }

        return false
    }
}
