package io.github.droidkaigi.confsched2018.presentation.feed

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.support.transition.TransitionInflater
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.UpdatingGroup
import com.xwray.groupie.ViewHolder
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.FragmentFeedBinding
import io.github.droidkaigi.confsched2018.di.Injectable
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.common.binding.FragmentDataBindingComponent
import io.github.droidkaigi.confsched2018.presentation.feed.item.FeedItem
import io.github.droidkaigi.confsched2018.util.ext.observe
import timber.log.Timber
import javax.inject.Inject

class FeedFragment : Fragment(), Injectable {

    private lateinit var binding: FragmentFeedBinding

    private val dataBindingComponent = FragmentDataBindingComponent(this)
    private val postsGroup = UpdatingGroup()

    private val feedItemCollapsed by lazy {
        ConstraintSet().apply {
            clone(context, R.layout.item_feed)
        }
    }
    private val feedItemExpanded by lazy {
        ConstraintSet().apply {
            clone(context, R.layout.item_feed)
            val buttonSize = resources.getDimensionPixelSize(R.dimen.feed_expand_button_size)
            setTransformPivot(R.id.expand_icon, buttonSize / 2F, buttonSize / 2F)
            setRotation(R.id.expand_icon, 180F)
        }
    }

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val postsViewModel: FeedViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(FeedViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentFeedBinding.inflate(inflater, container, false, dataBindingComponent)
        lifecycle.addObserver(postsViewModel)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycle.addObserver(postsViewModel)

        setupRecyclerView()

        postsViewModel.feeds.observe(this, { result ->
            when (result) {
                is Result.Success -> {
                    val posts = result.data
                    val expandTransition = TransitionInflater.from(context).inflateTransition(R.transition.expand_toggle)
                    postsGroup.update(posts.map { FeedItem(it, feedItemCollapsed, feedItemExpanded, expandTransition) })
                }
                is Result.Failure -> {
                    Timber.e(result.e)
                }
            }
        })
    }

    private fun setupRecyclerView() {
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            add(postsGroup)
            setOnItemClickListener({ item, view ->
                //TODO
            })
        }
        binding.feedRecycler.adapter = groupAdapter
    }


    companion object {
        fun newInstance(): FeedFragment = FeedFragment()
    }
}
