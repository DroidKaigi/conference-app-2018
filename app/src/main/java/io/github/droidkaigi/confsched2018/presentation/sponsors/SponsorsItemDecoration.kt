package io.github.droidkaigi.confsched2018.presentation.sponsors

import android.graphics.Rect
import android.support.annotation.LayoutRes
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import io.github.droidkaigi.confsched2018.util.ext.setPaddingWithLayoutDirection

class SponsorsItemDecoration(@LayoutRes private val sponsorItemViewType: Int)
    : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {
        if (!isSponsorItem(view, parent)) {
            return
        }

        val params = view.layoutParams as? GridLayoutManager.LayoutParams
        val layoutManager = parent.layoutManager as? GridLayoutManager

        val spanIndex = params?.spanIndex ?: 0
        val spanSize = params?.spanSize ?: 2
        val spanCount = layoutManager?.spanCount ?: SPONSOR_SCREEN_MAX_SPAN_SIZE

        val isStart = spanIndex == 0
        val isEnd = spanIndex + spanSize == spanCount

        // set padding evenly to each item
        val density = view.context.resources.displayMetrics.density
        val sidePadding = 16 * density
        val middlePadding = 8 * density

        val startPadding = when {
            isStart -> sidePadding
            isEnd && spanSize == 2 -> 0f
            isEnd && spanSize == 3 -> middlePadding / 2
            else -> middlePadding
        }.toInt()

        val endPadding = when {
            isEnd -> sidePadding
            isStart && spanSize == 2 -> 0f
            isStart && spanSize == 3 -> middlePadding / 2
            else -> middlePadding
        }.toInt()

        view.setPaddingWithLayoutDirection(
                startPadding, view.paddingTop, endPadding, view.paddingBottom)
    }

    private fun isSponsorItem(child: View, parent: RecyclerView): Boolean =
            parent.layoutManager?.getItemViewType(child) == sponsorItemViewType
}
