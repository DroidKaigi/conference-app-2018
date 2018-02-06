package io.github.droidkaigi.confsched2018.presentation.staff

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.SimpleItemAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import dagger.android.support.DaggerFragment
import io.github.droidkaigi.confsched2018.databinding.FragmentStaffBinding
import io.github.droidkaigi.confsched2018.presentation.NavigationController
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.staff.item.StaffItem
import io.github.droidkaigi.confsched2018.presentation.staff.item.StaffSection
import io.github.droidkaigi.confsched2018.util.ext.observe
import timber.log.Timber
import javax.inject.Inject

class StaffFragment : DaggerFragment() {

    private lateinit var binding: FragmentStaffBinding
    private val staffViewModel: StaffViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(StaffViewModel::class.java)
    }
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var navigationController: NavigationController
    private val staffSection = StaffSection()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentStaffBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupRecyclerView()
        staffViewModel.staff.observe(this, { result ->
            when (result) {
                is Result.Success -> {
                    val staff = result.data
                    staffSection.updateStaff(staff)
                }
                is Result.Failure -> {
                    Timber.e(result.e)
                }
            }
        })
        lifecycle.addObserver(staffViewModel)
    }

    private fun setupRecyclerView() {
        val groupAdapter = GroupAdapter<ViewHolder>().apply {
            add(staffSection)
            setOnItemClickListener({ item, _ ->
                if (item !is StaffItem) {
                    return@setOnItemClickListener
                }
                navigationController.navigateToExternalBrowser(item.staff.htmlUrl)
            })
        }
        binding.staffRecycler.apply {
            adapter = groupAdapter
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
    }

    companion object {
        fun newInstance(): StaffFragment = StaffFragment()
    }
}
