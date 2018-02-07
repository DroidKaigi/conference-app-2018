package io.github.droidkaigi.confsched2018.presentation.common.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.droidkaigi.confsched2018.databinding.ItemSpeakersSummaryLayoutBinding
import io.github.droidkaigi.confsched2018.model.Speaker
import io.github.droidkaigi.confsched2018.util.DebouncingOnClickListener

class SpeakersAdapter(var context: Context, var speakerList: List<Speaker>, val textColor: Int) :
        RecyclerView.Adapter<SpeakersAdapter.BindingHolder>() {
    var onSpeakerClick: (view: View, speakerId: String) -> Unit = { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BindingHolder? {
        val layoutInflater = LayoutInflater.from(parent!!.context)
        val binding = ItemSpeakersSummaryLayoutBinding.inflate(layoutInflater, parent, false)
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        val speaker = speakerList[position]
        holder.binding.linearLayout.setOnClickListener(object : DebouncingOnClickListener() {
            override fun doClick(v: View) = onSpeakerClick(v, speaker.id)
        })
        holder.binding.speakerName.setTextColor(textColor)
        holder.binding.speaker = speaker
    }

    override fun getItemCount(): Int {
        return speakerList.size
    }

    class BindingHolder(var binding: ItemSpeakersSummaryLayoutBinding)
        : RecyclerView.ViewHolder(binding.root)
}
