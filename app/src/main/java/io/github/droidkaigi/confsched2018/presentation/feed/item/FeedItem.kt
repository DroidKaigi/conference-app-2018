package io.github.droidkaigi.confsched2018.presentation.feed.item

import android.support.constraint.ConstraintSet
import android.support.transition.Transition
import android.support.transition.TransitionManager
import com.xwray.groupie.databinding.BindableItem
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ItemFeedBinding
import io.github.droidkaigi.confsched2018.model.Post

data class FeedItem(
        val post: Post,
        private val feedItemCollapsed: ConstraintSet,
        private val feedItemExpanded: ConstraintSet,
        private val expandTransition: Transition,
        private var expanded: Boolean = false
) : BindableItem<ItemFeedBinding>(
        post.hashCode().toLong()
) {

    override fun bind(viewBinding: ItemFeedBinding, position: Int) {
        viewBinding.post = post
        viewBinding.root.setOnClickListener {
            expanded = !expanded
            // FIXME: Animation!!
            TransitionManager.beginDelayedTransition(viewBinding.feedItemConstraintLayout, expandTransition)
            if (!expanded) {
                viewBinding.content.maxLines = 3
                feedItemCollapsed.applyTo(viewBinding.feedItemConstraintLayout)
            } else {
                viewBinding.content.maxLines = 15
                feedItemExpanded.applyTo(viewBinding.feedItemConstraintLayout)
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_feed
}
