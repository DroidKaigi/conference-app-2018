package io.github.droidkaigi.confsched2018.presentation.speaker

import android.annotation.TargetApi
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import androidx.animation.doOnEnd
import androidx.os.bundleOf
import androidx.transition.addListener
import androidx.transition.doOnStart
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import dagger.android.support.DaggerFragment
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.BottomSheetDialogSpeakerSnsBinding
import io.github.droidkaigi.confsched2018.databinding.FragmentSpeakerDetailBinding
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.model.Speaker
import io.github.droidkaigi.confsched2018.presentation.NavigationController
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.sessions.item.SimpleSessionsSection
import io.github.droidkaigi.confsched2018.presentation.sessions.item.SpeechSessionItem
import io.github.droidkaigi.confsched2018.util.SessionAlarm
import io.github.droidkaigi.confsched2018.util.ext.observe
import io.github.droidkaigi.confsched2018.util.ext.setLinearDivider
import io.github.droidkaigi.confsched2018.util.ext.toInvisible
import io.github.droidkaigi.confsched2018.util.ext.toVisible
import timber.log.Timber
import javax.inject.Inject

class SpeakerDetailFragment : DaggerFragment() {
    private lateinit var binding: FragmentSpeakerDetailBinding
    private lateinit var dialogBinding: BottomSheetDialogSpeakerSnsBinding
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private val sessionsSection = SimpleSessionsSection()
    private var isEnterTransitionCanceled: Boolean = false
    @Inject lateinit var navigationController: NavigationController
    @Inject lateinit var sessionAlarm: SessionAlarm

    private lateinit var bottomSheetDialog: BottomSheetDialog

    private val speakerDetailViewModel: SpeakerDetailViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SpeakerDetailViewModel::class.java)
    }

    private val revealViewRunnable = Runnable { reveal() }

    private val onFavoriteClickListener = { session: Session.SpeechSession ->
        speakerDetailViewModel.onFavoriteClick(session)
        sessionAlarm.toggleRegister(session)
    }

    private val onFeedbackListener = { session: Session.SpeechSession ->
        navigationController.navigateToSessionsFeedbackActivity(session)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentSpeakerDetailBinding.inflate(inflater, container!!, false)
        activity?.supportStartPostponedEnterTransition()

        dialogBinding = BottomSheetDialogSpeakerSnsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setupRecyclerView()
        speakerDetailViewModel.speakerId = arguments!!.getString(EXTRA_SPEAKER_ID)
        speakerDetailViewModel.speakerSessions.observe(this, { result ->
            when (result) {
                is Result.Success -> {
                    val speaker = result.data.first
                    val sessions = result.data.second
                    bindSpeaker(speaker, sessions)
                }
                is Result.Failure -> {
                    Timber.e(result.e)
                }
            }
        })

        bottomSheetDialog = BottomSheetDialog(context!!).apply {
            setContentView(dialogBinding.root)
            setCancelable(true)
        }

        if (arguments!!.getString(EXTRA_TRANSITION_NAME)?.isNotEmpty() == true) {
            initViewTransition(savedInstanceState)
        }
    }

    private fun bindSpeaker(speaker: Speaker, sessions: List<Session.SpeechSession>) {
        binding.speaker = speaker
        dialogBinding.speaker = speaker
        sessionsSection.updateSessions(sessions,
                onFavoriteClickListener,
                onFeedbackListener,
                userIdInDetail = speaker.id)
        binding.speakerImage.setOnClickListener {
            bottomSheetDialog.show()
        }

        val navigateBrowserAndDismissDialog: (String) -> Unit = { url ->
            navigationController.navigateToExternalBrowser(url)
            bottomSheetDialog.dismiss()
        }
        dialogBinding.speakerDetailTwitter.setOnClickListener {
            speaker.twitterUrl?.let(navigateBrowserAndDismissDialog)
        }
        dialogBinding.speakerDetailGithub.setOnClickListener {
            speaker.githubUrl?.let(navigateBrowserAndDismissDialog)
        }
        dialogBinding.speakerDetailCompany.setOnClickListener {
            speaker.companyUrl?.let(navigateBrowserAndDismissDialog)
        }
        dialogBinding.speakerDetailBlog.setOnClickListener {
            speaker.blogUrl?.let(navigateBrowserAndDismissDialog)
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initViewTransition(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return

        binding.appBarBackground.toInvisible()

        ViewCompat.setTransitionName(binding.speakerImage,
                arguments!!.getString(EXTRA_TRANSITION_NAME))

        val transitionInflater = TransitionInflater.from(activity)

        if (savedInstanceState == null) {
            activity?.window?.sharedElementEnterTransition = transitionInflater
                    .inflateTransition(R.transition.shared_element_arc)
                    .apply {
                        duration = 400
                        addListener(
                                onEnd = {
                                    if (!isEnterTransitionCanceled) {
                                        binding.root.post(revealViewRunnable)
                                    }
                                },
                                onPause = { isEnterTransitionCanceled = true },
                                onCancel = { isEnterTransitionCanceled = true }
                        )
                    }
        } else {
            binding.appBarBackground.toVisible()
        }

        activity?.window?.sharedElementReturnTransition = transitionInflater
                .inflateTransition(R.transition.shared_element_arc)
                .apply {
                    duration = 400
                    doOnStart { hideReveal() }
                }
    }

    private fun setupRecyclerView() {
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            add(sessionsSection)
            setOnItemClickListener({ item, _ ->
                val sessionItem = item as? SpeechSessionItem ?: return@setOnItemClickListener
                navigationController.navigateToSessionDetailActivity(sessionItem.session)
            })
        }
        val linearLayoutManager = LinearLayoutManager(context)
        binding.sessionsRecycler.apply {
            adapter = groupAdapter
            setLinearDivider(R.drawable.shape_divider_vertical_6dp, linearLayoutManager)
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun reveal() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return

        // reload recycler view
        binding.sessionsRecycler.adapter.notifyDataSetChanged()

        val revealView = binding.appBarBackground
        val cx = binding.speakerImage.run { (x + width / 2).toInt() }
        val cy = binding.speakerImage.run { (y + height / 2).toInt() }
        ViewAnimationUtils.createCircularReveal(revealView, cx, cy, 0F, revealView.width.toFloat())
                .apply {
                    duration = 400
                    revealView.toVisible()
                    start()
                }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun hideReveal() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return

        val revealView = binding.appBarBackground
        val cx = binding.speakerImage.run { (x + width / 2).toInt() }
        val cy = binding.speakerImage.run { (y + height / 2).toInt() }

        ViewAnimationUtils.createCircularReveal(revealView, cx, cy, revealView.width.toFloat(), 0F)
                .apply {
                    duration = 300
                    doOnEnd { view?.toInvisible() }
                    start()
                }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            activity?.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        view?.removeCallbacks(revealViewRunnable)
        super.onDestroyView()
    }

    companion object {
        const val EXTRA_SPEAKER_ID = "EXTRA_SPEAKER_ID"
        const val EXTRA_TRANSITION_NAME = "EXTRA_TRANSITION_NAME"
        fun newInstance(speakerId: String, transitionName: String?): SpeakerDetailFragment =
                SpeakerDetailFragment().apply {
                    arguments = bundleOf(
                            EXTRA_SPEAKER_ID to speakerId,
                            EXTRA_TRANSITION_NAME to transitionName
                    )
                }
    }
}
