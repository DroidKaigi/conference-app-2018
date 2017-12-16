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
import io.github.droidkaigi.confsched2018.databinding.FragmentRoomSessionsBinding
import io.github.droidkaigi.confsched2018.di.Injectable
import io.github.droidkaigi.confsched2018.model.Room
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.binding.FragmentDataBindingComponent
import io.github.droidkaigi.confsched2018.presentation.sessions.item.DateSessionsGroup
import io.github.droidkaigi.confsched2018.util.ext.observe
import timber.log.Timber
import javax.inject.Inject

class RoomSessionsFragment : Fragment(), Injectable {

    private lateinit var binding: FragmentRoomSessionsBinding
    private lateinit var roomName: String
    private val dataBindingComponent = FragmentDataBindingComponent(this)

    private val sessionsGroup = DateSessionsGroup(dataBindingComponent)

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val sessionsViewModel: RoomSessionsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(RoomSessionsViewModel::class.java)
    }

    private val onFavoriteClickListener = { session: Session ->
        sessionsViewModel.onFavoriteClick(session)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        roomName = arguments!!.getString(ARG_ROOM_NAME)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentRoomSessionsBinding.inflate(inflater, container, false, dataBindingComponent)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(sessionsViewModel)

        binding.sessionsRecycler.adapter = GroupAdapter<ViewHolder>().apply {
            add(sessionsGroup)
            setOnItemClickListener({ item, view ->
                //TODO
            })
        }

        sessionsViewModel.roomName = roomName
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
        private val ARG_ROOM_NAME = "room_name"

        fun newInstance(room: Room): RoomSessionsFragment = RoomSessionsFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_ROOM_NAME, room.name)
            }
        }
    }
}
