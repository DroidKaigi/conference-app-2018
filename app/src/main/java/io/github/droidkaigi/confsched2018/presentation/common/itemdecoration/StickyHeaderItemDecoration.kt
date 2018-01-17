package io.github.droidkaigi.confsched2018.presentation.common.itemdecoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.TextPaint
import android.text.TextUtils
import android.view.View
import io.github.droidkaigi.confsched2018.R

/**
 * Created by e10dokup on 2018/01/18.
 */
class StickyHeaderItemDecoration constructor(
        context: Context?,
        private val callback: Callback
): RecyclerView.ItemDecoration() {

    private val textPaint = TextPaint()
    private val paint = Paint()
    private val marginLeft: Int
    private val marginRight: Int
    private val marginTop: Int
    private val fontMetrics = Paint.FontMetrics()

    init {
        val resource = context!!.resources

        textPaint.apply {
            typeface = Typeface.DEFAULT
            isAntiAlias = true
            textSize = 24f
            color = ContextCompat.getColor(context, R.color.primary)
            getFontMetrics(this.fontMetrics)
            textAlign = Paint.Align.LEFT
        }

        marginLeft = resource.getDimensionPixelSize(R.dimen.sticky_header_margin_left)
        marginRight = resource.getDimensionPixelSize(R.dimen.sticky_header_margin_right)
        marginTop = resource.getDimensionPixelSize(R.dimen.sticky_header_margin_top)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

//        parent ?: return

        val position = parent.getChildAdapterPosition(view)
        val groupId = callback.getGroupId(position)

        if (groupId < 0) {
            return
        }

        outRect.apply {
            left = marginLeft
            right = marginRight
            top = if (position == 0 || isFirstInGroup(position)) marginTop else 0
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        val totalItemCount = state.itemCount
        val childCount = parent.childCount
        val paddingLeft = parent.paddingLeft.toFloat()
        val lineHeight = textPaint.textSize + fontMetrics.descent

        var previousGroupId: Long = -1
        var groupId: Long = -1

        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)

            previousGroupId = groupId
            groupId = callback.getGroupId(position)

            if (groupId < 0 || previousGroupId == groupId) continue

            val textLine = callback.getGroupFirstLine(position)?.toUpperCase()
            if (TextUtils.isEmpty(textLine)) continue

            val viewBottom = view.bottom + view.paddingBottom
            var textY = Math.max(marginTop, view.top + view.paddingTop).toFloat()
            if (position + 1 < totalItemCount) {
                val nextGroupId = callback.getGroupId(position + 1)
                if (nextGroupId != groupId && viewBottom < textY + lineHeight) {
                    textY = viewBottom - lineHeight
                }
            }

            c.drawText(textLine, paddingLeft, textY, textPaint)
        }

    }

    private fun isFirstInGroup(position: Int): Boolean {
        return if (position == 0) {
            true
        } else {
            val previousGroupId = callback.getGroupId(position - 1)
            val groupId = callback.getGroupId(position)
            previousGroupId != groupId
        }
    }

    interface Callback {
        fun getGroupId(position: Int): Long
        fun getGroupFirstLine(position: Int): String?
    }
}
