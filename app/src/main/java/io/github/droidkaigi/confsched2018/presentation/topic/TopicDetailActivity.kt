package io.github.droidkaigi.confsched2018.presentation.topic

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ActivityTopicDetailBinding
import io.github.droidkaigi.confsched2018.model.Lang
import io.github.droidkaigi.confsched2018.model.Topic
import io.github.droidkaigi.confsched2018.presentation.NavigationController
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.common.menu.DrawerMenu
import io.github.droidkaigi.confsched2018.util.ext.observe
import io.github.droidkaigi.confsched2018.util.lang
import timber.log.Timber
import javax.inject.Inject

class TopicDetailActivity : DaggerAppCompatActivity() {

    @Inject lateinit var navigationController: NavigationController
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var drawerMenu: DrawerMenu

    private val binding: ActivityTopicDetailBinding by lazy {
        DataBindingUtil
                .setContentView<ActivityTopicDetailBinding>(this, R.layout.activity_topic_detail)
    }

    private val topicDetailViewModel: TopicDetailViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(TopicDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowTitleEnabled(false)
        }

        topicDetailViewModel.topicSessions.observe(this, { result ->
            when (result) {
                is Result.Success -> {
                    updateAppBarLayout(result.data.first, result.data.second.size)
                }
                is Result.Failure -> {
                    Timber.e(result.e)
                }
            }
        })

        binding.appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val factor = (-verticalOffset).toFloat() / appBarLayout.totalScrollRange.toFloat()
            binding.toolbarTextColorFactor = factor
        }

        navigationController.navigateToTopicDetail(intent.getIntExtra(EXTRA_TOPIC_ID, 0))
        drawerMenu.setup(binding.drawerLayout, binding.drawer, binding.toolbar)
    }

    override fun onBackPressed() {
        if (drawerMenu.closeDrawerIfNeeded()) {
            super.onBackPressed()
        }
    }

    private fun updateAppBarLayout(topic: Topic, total: Int) {
        binding.total = resources.getQuantityString(R.plurals.topic_total_session, total, total)
        if (lang() == Lang.JA) {
            binding.name = topic.getNameByLang(Lang.JA)
            binding.translation = topic.getNameByLang(Lang.EN)
        } else {
            binding.name = topic.getNameByLang(Lang.EN)
            binding.translation = topic.getNameByLang(Lang.JA)
        }
    }

    companion object {
        const val EXTRA_TOPIC_ID = "EXTRA_TOPIC_ID"
        fun start(context: Context, topicId: Int) {
            context.startActivity(Intent(context, TopicDetailActivity::class.java).apply {
                putExtra(EXTRA_TOPIC_ID, topicId)
            })
        }
    }
}
