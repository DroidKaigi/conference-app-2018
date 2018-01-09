package io.github.droidkaigi.confsched2018.presentation.detail

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.ActivitySessionDetailBinding
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.presentation.NavigationController
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.common.activity.BaseActivity
import io.github.droidkaigi.confsched2018.presentation.common.menu.DrawerMenu
import io.github.droidkaigi.confsched2018.util.CustomGlideApp
import io.github.droidkaigi.confsched2018.util.ext.observe
import io.github.droidkaigi.confsched2018.util.ext.toGone
import io.github.droidkaigi.confsched2018.util.ext.toVisible
import io.github.droidkaigi.confsched2018.util.lang
import timber.log.Timber
import javax.inject.Inject

class SessionDetailActivity : BaseActivity(), HasSupportFragmentInjector {
    @Inject lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    @Inject lateinit var navigationController: NavigationController
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var drawerMenu: DrawerMenu

    private val binding: ActivitySessionDetailBinding by lazy {
        DataBindingUtil
                .setContentView<ActivitySessionDetailBinding>(
                        this,
                        R.layout.activity_session_detail
                )
    }

    private val sessionDetailViewModel: SessionDetailViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SessionDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sessionDetailViewModel.sessionId =
                intent!!.getStringExtra(SessionDetailFragment.EXTRA_SESSION_ID)

        sessionDetailViewModel.session.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    val session = result.data
                    bindSessionData(session)
                }
                is Result.Failure -> {
                    Timber.e(result.e)
                }
            }
        }

        navigationController.navigateToDetail(intent.getStringExtra(EXTRA_SESSION_ID))
        drawerMenu.setup(binding.toolbar, binding.drawerLayout, binding.drawer)
    }

    fun bindSessionData(session: Session.SpeechSession) {
        binding.session = session

        binding.fab.setOnClickListener {
            sessionDetailViewModel.onFavoriteClick(session)
        }

        binding.sessionTopic.text = session.topic.getNameByLang(lang())
        val speakerImages = arrayOf(
                binding.speakerImage1,
                binding.speakerImage2,
                binding.speakerImage3,
                binding.speakerImage4,
                binding.speakerImage5
        )
        speakerImages.forEachIndexed { index, imageView ->
            if (index < session.speakers.size) {
                imageView.toVisible()
                val size = resources.getDimensionPixelSize(R.dimen.speaker_image)
                CustomGlideApp
                        .with(this)
                        .load(session.speakers[index].imageUrl)
                        .placeholder(R.drawable.ic_person_black_24dp)
                        .override(size, size)
                        .dontAnimate()
                        .transform(CircleCrop())
                        .into(imageView)
            } else {
                imageView.toGone()
            }
        }

        binding.speakers.text = session.speakers.joinToString { it.name }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingAndroidInjector

    companion object {
        val EXTRA_SESSION_ID = "EXTRA_SESSION_ID"
        fun start(context: Context, session: Session) {
            context.startActivity(Intent(context, SessionDetailActivity::class.java).apply {
                putExtra(EXTRA_SESSION_ID, session.id)
            })
        }
    }
}
