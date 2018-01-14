package io.github.droidkaigi.confsched2018.util.ext

import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import io.github.droidkaigi.confsched2018.R

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

fun RecyclerView.addDivider() {
    this.addItemDecoration(DividerItemDecoration(this.context, LinearLayoutManager(this.context).orientation).apply {
        setDrawable(ContextCompat.getDrawable(this@addDivider.context, R.drawable.shape_divider_vertical_6dp)!!)
    })
}
