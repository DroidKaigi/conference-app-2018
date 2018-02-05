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
        private val onClickUri: (String) -> Unit
) : BindableItem<ItemFeedBinding>(
        post.hashCode().toLong()
) {
    private val touchIgnore = View.OnTouchListener { _, _ -> true }
    private var initialized: Boolean = false
    private var expanded: Boolean = false

    override fun bind(viewBinding: ItemFeedBinding, position: Int) {
        viewBinding.post = post

        viewBinding.feedIcon.setImageResource(when (post.type) {
            Post.Type.TUTORIAL -> R.drawable.ic_feed_tutorial_pink_20dp
            Post.Type.NOTIFICATION -> R.drawable.ic_feed_notification_blue_20dp
            Post.Type.ALERT -> R.drawable.ic_feed_alert_amber_20dp
            Post.Type.FEEDBACK -> R.drawable.ic_feed_feedback_cyan_20dp
        })

        viewBinding.content.onClickUrl = onClickUri

        viewBinding.content.viewTreeObserver.apply {
            addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    if (isAlive) removeOnPreDrawListener(this)

                    val expandable: Boolean

                    if (!initialized) {
                        initialized = true
                        viewBinding.title.maxLines = 1
                        viewBinding.content.maxLines = 1

                        expandable = viewBinding.content.lineCount > 1
                        viewBinding.expandable = expandable
                    } else {
                        expandable = viewBinding.expandable!!
                    }

                    if (expandable) {
                        viewBinding.root.setOnClickListener {
                            expanded = !expanded

                            if (expanded) {
                                prepareAnimation(expandTransition.clone(), viewBinding)
                                startExpandAnimation(viewBinding)
                            } else {
                                prepareAnimation(collapseTransition.clone(), viewBinding)
                                startCollapseAnimation(viewBinding)
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

    private fun prepareAnimation(transition: Transition, viewBinding: ItemFeedBinding) {
        val parent = viewBinding.root.parent as RecyclerView
        parent.setOnTouchListener(touchIgnore)

        transition.addListener(object : TransitionListenerAdapter() {
            override fun onTransitionEnd(transition: Transition) {
                parent.setOnTouchListener(null)
            }
        })
        TransitionManager.beginDelayedTransition(parent, transition)
    }

    private fun startExpandAnimation(viewBinding: ItemFeedBinding) {
        viewBinding.title.maxLines = Integer.MAX_VALUE
        viewBinding.content.maxLines = Integer.MAX_VALUE
        viewBinding.title.setTextColor(ResourcesCompat.getColor(
                viewBinding.context.resources, R.color.primary, null))
        viewBinding.expandIcon.setImageResource(R.drawable.ic_expand_more_primary_24dp)

        feedItemExpanded.applyTo(viewBinding.feedItemConstraintLayout)
    }

    private fun startCollapseAnimation(viewBinding: ItemFeedBinding) {
        viewBinding.title.maxLines = 1
        viewBinding.content.maxLines = 1
        viewBinding.title.setTextColor(ResourcesCompat.getColor(
                viewBinding.context.resources, R.color.black, null))
        viewBinding.expandIcon.setImageResource(R.drawable.ic_expand_more_black_24dp)

        feedItemCollapsed.applyTo(viewBinding.feedItemConstraintLayout)
    }

    override fun getLayout(): Int = R.layout.item_feed
}
