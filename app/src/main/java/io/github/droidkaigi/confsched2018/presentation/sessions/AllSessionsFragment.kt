package io.github.droidkaigi.confsched2018.presentation.sessions

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.di.Injectable
import io.github.droidkaigi.confsched2018.presentation.Result
import javax.inject.Inject

class AllSessionsFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var sessionsViewModel: AllSessionsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_all_sessions, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sessionsViewModel = ViewModelProviders.of(this, viewModelFactory).get(AllSessionsViewModel::class.java)

        sessionsViewModel.sessions.observe(this, Observer {
            when (it) {
                is Result.Success -> {
                    Toast.makeText(context, "size : " + it.data.size, Toast.LENGTH_LONG).show()
                }
            }
        })
        lifecycle.addObserver(sessionsViewModel)
    }

    companion object {
        fun newInstance(): AllSessionsFragment = AllSessionsFragment()
    }
}
