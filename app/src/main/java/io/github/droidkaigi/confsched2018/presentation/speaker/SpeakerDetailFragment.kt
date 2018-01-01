package io.github.droidkaigi.confsched2018.presentation.speaker

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.github.droidkaigi.confsched2018.databinding.FragmentSpeakerDetailBinding
import io.github.droidkaigi.confsched2018.di.Injectable
import javax.inject.Inject

class SpeakerDetailFragment : Fragment(), Injectable {
    private lateinit var binding: FragmentSpeakerDetailBinding
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentSpeakerDetailBinding.inflate(inflater, container!!, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Toast.makeText(context, arguments!!.getString(EXTRA_SPEAKER_ID), Toast.LENGTH_LONG).show()
    }

    companion object {
        const val EXTRA_SPEAKER_ID = "EXTRA_SPEAKER_ID"
        fun newInstance(speakerId: String): SpeakerDetailFragment = SpeakerDetailFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_SPEAKER_ID, speakerId)
            }
        }
    }
}
