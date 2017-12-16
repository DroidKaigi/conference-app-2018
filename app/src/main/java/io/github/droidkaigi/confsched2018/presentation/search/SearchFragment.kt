package io.github.droidkaigi.confsched2018.presentation.search

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import io.github.droidkaigi.confsched2018.R
import io.github.droidkaigi.confsched2018.databinding.FragmentSearchBinding
import io.github.droidkaigi.confsched2018.di.Injectable
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.binding.FragmentDataBindingComponent
import io.github.droidkaigi.confsched2018.presentation.sessions.item.DateSessionsGroup
import io.github.droidkaigi.confsched2018.util.ext.observe
import timber.log.Timber
import javax.inject.Inject

class SearchFragment : Fragment(), Injectable {
    private lateinit var binding: FragmentSearchBinding
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val dataBindingComponent = FragmentDataBindingComponent(this)

    private val sessionsGroup = DateSessionsGroup(dataBindingComponent)

    private val searchViewModel: SearchViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel::class.java)
    }

    private val onFavoriteClickListener = { session: Session ->
        searchViewModel.onFavoriteClick(session)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity!! as AppCompatActivity).supportActionBar!!.title = ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchRecycler.adapter = GroupAdapter<ViewHolder>().apply {
            add(sessionsGroup)
            setOnItemClickListener({ item, view ->
                //TODO
            })
        }

        searchViewModel.sessions.observe(this, { result ->
            when (result) {
                is Result.Success -> {
                    val sessions = result.data
                    sessionsGroup.updateSessions(sessions, onFavoriteClickListener)
                    binding.searchRecycler.scrollToPosition(0)
                }
                is Result.Failure -> {
                    Timber.e(result.e)
                }
            }
        })
        lifecycle.addObserver(searchViewModel)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.search, menu)
        val menuSearchItem = menu!!.findItem(R.id.action_search)
        menuSearchItem.expandActionView()

        val searchView = menuSearchItem.actionView as SearchView
        searchView.isIconified = false
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                searchViewModel.onQuery(newText.orEmpty())
                return false
            }
        })
    }

    companion object {
        fun newInstance(): SearchFragment = SearchFragment()
    }

}
