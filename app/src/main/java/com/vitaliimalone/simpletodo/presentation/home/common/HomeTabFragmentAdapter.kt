package com.vitaliimalone.simpletodo.presentation.home.common

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vitaliimalone.simpletodo.presentation.hometab.HomeTabFragment

class HomeTabFragmentAdapter(
    private val fragments: Array<HomeTab>, fragment: Fragment
) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        return HomeTabFragment.newInstance(fragments[position])
    }

    override fun getItemCount(): Int {
        return fragments.size
    }
}