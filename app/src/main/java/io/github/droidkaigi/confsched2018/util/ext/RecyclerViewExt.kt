package io.github.droidkaigi.confsched2018.util.ext

import android.support.v7.widget.RecyclerView

fun RecyclerView.addOnScrollListener(
        onScrollStateChanged: (recyclerView: RecyclerView?, newState: Int) -> Unit,
        onScrolled: (recyclerView: RecyclerView?, dx: Int, dy: Int) -> Unit
) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            onScrollStateChanged(recyclerView, newState)
        }

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            onScrolled(recyclerView, dx, dy)
        }
    })
}
