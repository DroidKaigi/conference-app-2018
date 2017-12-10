package io.github.droidkaigi.confsched2018.presentation.sessions

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.droidkaigi.confsched2018.databinding.RowSessionBinding
import io.github.droidkaigi.confsched2018.model.Session

class SessionsAdapter : RecyclerView.Adapter<SessionsAdapter.SessionViewHolder>() {
    var sessions = listOf<Session>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SessionViewHolder {
        val binding = RowSessionBinding.inflate(LayoutInflater.from(parent!!.context), parent, false)
        return SessionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SessionViewHolder?, position: Int) {
        val sessionViewHolder = holder!!
        sessionViewHolder.binding.session = sessions[position]
        sessionViewHolder.binding.speakers.text = sessions[position].speakers.joinToString { it.name }
    }

    override fun getItemCount(): Int = sessions.size


    class SessionViewHolder(val binding: RowSessionBinding) : RecyclerView.ViewHolder(binding.root) {
    }
}
