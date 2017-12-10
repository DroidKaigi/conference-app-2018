package io.github.droidkaigi.confsched2018.presentation.sessions

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.droidkaigi.confsched2018.databinding.FragmentRoomSessionsBinding
import io.github.droidkaigi.confsched2018.di.Injectable
import io.github.droidkaigi.confsched2018.model.Room
import io.github.droidkaigi.confsched2018.presentation.Result
import javax.inject.Inject

class RoomSessionsFragment : Fragment(), Injectable {

    private lateinit var roomName: String
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val sessionsViewModel: RoomSessionsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(RoomSessionsViewModel::class.java)
    }
    private lateinit var adapter: SessionsAdapter
    private lateinit var binding: FragmentRoomSessionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        roomName = arguments!!.getString(ARG_ROOM_NAME)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentRoomSessionsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SessionsAdapter()
        binding.sessionsRecycler.adapter = adapter


        sessionsViewModel.roomName = roomName
        sessionsViewModel.sessions.observe(this, Observer { result ->
            when (result) {
                is Result.Success -> {
                    adapter.sessions = result.data
                }
            }
        })
        lifecycle.addObserver(sessionsViewModel)

    }

    companion object {
        private val ARG_ROOM_NAME = "room_name"

        fun newInstance(room: Room): RoomSessionsFragment = RoomSessionsFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_ROOM_NAME, room.name)
            }
        }
    }
}
