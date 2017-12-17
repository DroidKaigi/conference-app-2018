package io.github.droidkaigi.confsched2018.presentation.common.binding

import android.databinding.DataBindingComponent
import android.support.v4.app.Fragment

class FragmentDataBindingComponent(fragment: Fragment) : DataBindingComponent {
    private val fragmentBindingAdapters: FragmentBindingAdapters = FragmentBindingAdapters(fragment)
    override fun getFragmentBindingAdapters(): FragmentBindingAdapters = fragmentBindingAdapters


}
