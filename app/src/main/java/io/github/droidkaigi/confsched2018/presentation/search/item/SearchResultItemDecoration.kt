package io.github.droidkaigi.confsched2018.presentation.search.item

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import io.github.droidkaigi.confsched2018.R

class SearchResultItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val itemIndex = parent.getChildAdapterPosition(view)

        if (itemIndex == 0) {
            val topMargin = view.resources.getDimensionPixelSize(R.dimen.search_result_top_margin)
            outRect.set(0, topMargin, 0, 0)
        }
    }
}
