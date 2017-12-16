package io.github.droidkaigi.confsched2018.presentation.sessions

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import io.github.droidkaigi.confsched2018.databinding.FragmentAllSessionsBinding
import io.github.droidkaigi.confsched2018.di.Injectable
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.binding.FragmentDataBindingComponent
import io.github.droidkaigi.confsched2018.presentation.sessions.item.DateSessionsGroup
import io.github.droidkaigi.confsched2018.util.ext.observe
import timber.log.Timber
import javax.inject.Inject

class AllSessionsFragment : Fragment(), Injectable {

    private lateinit var binding: FragmentAllSessionsBinding

    private val dataBindingComponent = FragmentDataBindingComponent(this)
    private val sessionsGroup = DateSessionsGroup(dataBindingComponent)

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val sessionsViewModel: AllSessionsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(AllSessionsViewModel::class.java)
    }

    private val onFavoriteClickListener = { session: Session ->
        sessionsViewModel.onFavoriteClick(session)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentAllSessionsBinding.inflate(inflater, container, false, dataBindingComponent)
        lifecycle.addObserver(sessionsViewModel)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycle.addObserver(sessionsViewModel)

        binding.sessionsRecycler.adapter = GroupAdapter<ViewHolder>().apply {
            add(sessionsGroup)
            setOnItemClickListener({ item, view ->
                //TODO
            })
        }

        sessionsViewModel.sessions.observe(this, { result ->
            when (result) {
                is Result.Success -> {
                    val sessions = result.data
                    sessionsGroup.updateSessions(sessions, onFavoriteClickListener)
                }
                is Result.Failure -> {
                    Timber.e(result.e)
                }
            }
        })
    }

    companion object {
        fun newInstance(): AllSessionsFragment = AllSessionsFragment()
    }
}
