package io.github.droidkaigi.confsched2018.presentation.sessions

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.droidkaigi.confsched2018.databinding.RowSessionBinding
import io.github.droidkaigi.confsched2018.model.Session

class SessionsAdapter(private val onFavoriteClickListener: (Session) -> Unit = {}) : RecyclerView.Adapter<SessionsAdapter.SessionViewHolder>() {

    var sessions = listOf<Session>()
        set(value) {
            val old = field
            field = value
            DiffUtil.calculateDiff(diffCallback(old)).dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SessionViewHolder {
        val binding = RowSessionBinding.inflate(LayoutInflater.from(parent!!.context), parent, false)
        return SessionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SessionViewHolder?, position: Int) {
        val sessionViewHolder = holder!!
        val session = sessions[position]
        sessionViewHolder.binding.session = session
        sessionViewHolder.binding.speakers.text = session.speakers.joinToString { it.name }
        sessionViewHolder.binding.favorite.setOnClickListener {
            onFavoriteClickListener(session)
        }

    }

    override fun getItemCount(): Int = sessions.size

    class SessionViewHolder(val binding: RowSessionBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    private fun diffCallback(old: List<Session>): DiffUtil.Callback {
        return object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                    sessions[oldItemPosition].id == sessions[newItemPosition].id

            override fun getOldListSize(): Int = old.size

            override fun getNewListSize(): Int = sessions.size

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                    sessions[oldItemPosition] == sessions[newItemPosition]
        }
    }


}
