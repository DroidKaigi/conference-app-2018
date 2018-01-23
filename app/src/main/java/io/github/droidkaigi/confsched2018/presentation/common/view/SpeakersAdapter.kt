package io.github.droidkaigi.confsched2018.presentation.common.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.droidkaigi.confsched2018.databinding.ItemSpeakersSummaryLayoutBinding
import io.github.droidkaigi.confsched2018.model.Speaker

/**
 * Created by furusin on 2018/01/17.
 */
class SpeakersAdapter(var context: Context, var speakerList: List<Speaker>, val textColor: Int) :
        RecyclerView.Adapter<SpeakersAdapter.BindingHolder>() {
    lateinit var listener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BindingHolder? {
        val layoutInflater = LayoutInflater.from(parent!!.context)
        setOnItemClickListener(listener)
        val binding = ItemSpeakersSummaryLayoutBinding.inflate(layoutInflater, parent, false)
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        val speaker = speakerList[position]
        holder.binding.linearLayout.setOnClickListener({
            listener.onClick(it, speaker.id)
        })
        holder.binding.speakerName.setTextColor(textColor)
        holder.binding.speaker = speaker
    }

    override fun getItemCount(): Int {
        return speakerList.size
    }

    interface OnItemClickListener {
        fun onClick(view: View, speakerId: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    class BindingHolder(var binding: ItemSpeakersSummaryLayoutBinding)
        : RecyclerView.ViewHolder(binding.root)
}
