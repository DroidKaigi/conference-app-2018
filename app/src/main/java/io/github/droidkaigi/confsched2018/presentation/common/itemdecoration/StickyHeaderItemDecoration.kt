package io.github.droidkaigi.confsched2018.presentation.common.itemdecoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.text.TextPaint
import android.text.TextUtils
import android.view.View
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.util.ext.isLayoutDirectionRtl

/**
 * Created by e10dokup on 2018/01/18.
 */
class StickyHeaderItemDecoration constructor(
        context: Context?,
        private val callback: Callback
) : RecyclerView.ItemDecoration() {

    private val textPaint = TextPaint()
    private val labelPadding: Int
    private val contentMargin: Int
    private val fontMetrics: Paint.FontMetrics

    init {
        val resource = context!!.resources

        textPaint.apply {
            typeface = ResourcesCompat.getFont(context, R.font.notosans_medium)
            isAntiAlias = true
            textSize = resource.getDimension(R.dimen.sticky_label_font_size)
            color = ContextCompat.getColor(context, R.color.primary)
            textAlign = Paint.Align.LEFT
        }

        fontMetrics = textPaint.fontMetrics

        labelPadding = resource.getDimensionPixelSize(R.dimen.sticky_label_padding)
        contentMargin = resource.getDimensionPixelSize(R.dimen.sticky_label_content_margin)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        val groupId = callback.getGroupId(position)

        if (groupId < 0) {
            return
        }

        if (view.isLayoutDirectionRtl()) {
            outRect.right = contentMargin
        } else {
            outRect.left = contentMargin
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        val totalItemCount = state.itemCount
        val childCount = parent.childCount
        val lineHeight = textPaint.textSize + fontMetrics.descent
        var previousGroupId: Long
        var groupId: Long = -1

        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(view)

            previousGroupId = groupId
            groupId = callback.getGroupId(position)

            if (groupId < 0 || previousGroupId == groupId) continue

            val textLine = callback.getGroupFirstLine(position)
            if (TextUtils.isEmpty(textLine)) continue

            val textX = if (view.isLayoutDirectionRtl()) {
                labelPadding.toFloat() + view.width.toFloat()
            } else {
                labelPadding.toFloat()
            }

            val viewBottom = view.bottom + view.paddingBottom
            var textY = Math.max(view.height, viewBottom) - lineHeight
            if (position + 1 < totalItemCount) {
                val nextGroupId = callback.getGroupId(position + 1)
                if (nextGroupId != groupId && viewBottom < textY + lineHeight) {
                    textY = viewBottom - lineHeight
                }
            }
            c.drawText(textLine, textX, textY, textPaint)
        }
    }

    interface Callback {
        fun getGroupId(position: Int): Long
        fun getGroupFirstLine(position: Int): String?
    }
}
