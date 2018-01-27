package io.github.droidkaigi.confsched2018.presentation.feed.item

import android.support.constraint.ConstraintSet
import android.support.transition.Transition
import android.support.transition.TransitionListenerAdapter
import android.support.transition.TransitionManager
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewTreeObserver
import com.xwray.groupie.databinding.BindableItem
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemFeedBinding
import io.github.droidkaigi.confsched2018.model.Post
import io.github.droidkaigi.confsched2018.util.ext.context

data class FeedItem(
        val post: Post,
        private val feedItemCollapsed: ConstraintSet,
        private val feedItemExpanded: ConstraintSet,
        private val expandTransition: Transition,
        private val collapseTransition: Transition,
        private var initialized: Boolean = false,
        private var expanded: Boolean = false
) : BindableItem<ItemFeedBinding>(
        post.hashCode().toLong()
) {
    private val touchIgnore = View.OnTouchListener { _, _ -> true }

    override fun bind(viewBinding: ItemFeedBinding, position: Int) {
        viewBinding.post = post

        viewBinding.feedIcon.setImageResource(when (post.type) {
            "tutorial" -> R.drawable.ic_feed_tutorial_pink_20dp
            "notification" -> R.drawable.ic_feed_notification_blue_20dp
            "alert" -> R.drawable.ic_feed_alert_amber_20dp
            "enquete" -> R.drawable.ic_feed_enquete_cyan_20dp
            else -> R.drawable.ic_feed_notification_blue_20dp
        })

        viewBinding.content.viewTreeObserver.apply {
            addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    if (isAlive) removeOnPreDrawListener(this)

                    val expandable: Boolean

                    if (!initialized) {
                        initialized = true
                        viewBinding.content.maxLines = 1

                        expandable = viewBinding.content.lineCount > 1
                        viewBinding.expandable = expandable
                    } else {
                        expandable = viewBinding.expandable!!
                    }

                    if (expandable) {
                        viewBinding.root.setOnClickListener {
                            val parent = viewBinding.root.parent as RecyclerView
                            parent.setOnTouchListener(touchIgnore)

                            val transition = when (expanded) {
                                true -> collapseTransition
                                false -> expandTransition
                            }.clone()

                            transition.addListener(object : TransitionListenerAdapter() {
                                override fun onTransitionEnd(transition: Transition) {
                                    parent.setOnTouchListener(null)
                                }
                            })
                            TransitionManager.beginDelayedTransition(parent, transition)
                            expanded = !expanded

                            viewBinding.content.maxLines = if (expanded) Integer.MAX_VALUE else 1

                            val titleTextColor = ResourcesCompat.getColor(
                                    viewBinding.context.resources,
                                    if (expanded) R.color.primary else R.color.black,
                                    null)
                            viewBinding.title.setTextColor(titleTextColor)

                            viewBinding.expandIcon.setImageResource(if (expanded) {
                                R.drawable.ic_expand_more_primary_24dp
                            } else {
                                R.drawable.ic_expand_more_black_24dp
                            })

                            if (!expanded) {
                                feedItemCollapsed.applyTo(viewBinding.feedItemConstraintLayout)
                            } else {
                                feedItemExpanded.applyTo(viewBinding.feedItemConstraintLayout)
                            }
                        }
                    } else {
                        viewBinding.root.setOnClickListener(null)
                    }

                    return true
                }
            })
        }
    }

    override fun getLayout(): Int = R.layout.item_feed
}
