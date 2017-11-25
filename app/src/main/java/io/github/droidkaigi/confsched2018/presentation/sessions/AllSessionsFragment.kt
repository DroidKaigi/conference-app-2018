package io.github.droidkaigi.confsched2018.presentation.sessions

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.di.Injectable

class AllSessionsFragment : Fragment(), Injectable {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_all_sessions, container, false)

    companion object {
        fun newInstance(): AllSessionsFragment = AllSessionsFragment()
    }
}
