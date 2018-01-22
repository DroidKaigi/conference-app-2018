package io.github.droidkaigi.confsched2018.presentation.feed

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.support.transition.TransitionInflater
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.analytics.FirebaseAnalytics
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.FragmentFeedBinding
import io.github.droidkaigi.confsched2018.di.Injectable
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.feed.item.FeedItem
import io.github.droidkaigi.confsched2018.util.ext.observe
import io.github.droidkaigi.confsched2018.util.ext.setLinearDivider
import timber.log.Timber
import javax.inject.Inject

class FeedFragment : Fragment(), Injectable {

    private lateinit var binding: FragmentFeedBinding

    private val postsSection = Section()
    private var fireBaseAnalytics: FirebaseAnalytics? = null

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

            constrainHeight(R.id.content, ConstraintSet.WRAP_CONTENT)
        }
    }

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val postsViewModel: FeedViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(FeedViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fireBaseAnalytics = FirebaseAnalytics.getInstance(context)
        setupRecyclerView()

        postsViewModel.feeds.observe(this, { result ->
            when (result) {
                is Result.Success -> {
                    val posts = result.data
                    val inflater = TransitionInflater.from(context)
                    val expandTransition = inflater.inflateTransition(R.transition.feed_item_expand)
                    val collapseTransition =
                            inflater.inflateTransition(R.transition.feed_item_collapse)
                    postsSection.update(posts
                            .map {
                                FeedItem(
                                        it,
                                        feedItemCollapsed,
                                        feedItemExpanded,
                                        expandTransition,
                                        collapseTransition
                                )
                            })
                }
                is Result.Failure -> {
                    Timber.e(result.e)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        fireBaseAnalytics?.setCurrentScreen(activity!!, null, this::class.java.simpleName)
    }

    private fun setupRecyclerView() {
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            add(postsSection)
            setOnItemClickListener({ _, _ ->
                //TODO
            })
        }
        val linearLayoutManager = LinearLayoutManager(context)
        binding.feedRecycler.apply {
            adapter = groupAdapter
            setLinearDivider(R.drawable.shape_divider_vertical_6dp, linearLayoutManager)
        }
    }

    companion object {
        fun newInstance(): FeedFragment = FeedFragment()
    }
}
