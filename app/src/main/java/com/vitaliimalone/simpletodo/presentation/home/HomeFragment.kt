package com.vitaliimalone.simpletodo.presentation.home

import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.vitaliimalone.simpletodo.R
import com.vitaliimalone.simpletodo.presentation.base.BaseFragment
import com.vitaliimalone.simpletodo.presentation.home.common.HomeTabFragmentAdapter
import com.vitaliimalone.simpletodo.presentation.models.HomeTab
import com.vitaliimalone.simpletodo.presentation.utils.DateTimeUtils
import com.vitaliimalone.simpletodo.presentation.utils.forceShowKeyboard
import kotlinx.android.synthetic.main.home_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment(R.layout.home_fragment) {
    private val viewModel: HomeViewModel by viewModel()
    private val tasksPagerAdapter by lazy {
        HomeTabFragmentAdapter(listOf(
                HomeTab.TODAY, HomeTab.WEEK, HomeTab.MONTH, HomeTab.TODO), this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupClickListeners()
        setupViews()
        setupObservers()
    }

    private fun setupClickListeners() {
        addFab.setOnClickListener {
            forceShowKeyboard()
            viewModel.addNewTestTask()
        }
    }

    private fun setupViews() {
        tasksViewPager.isUserInputEnabled = false
        tasksViewPager.adapter = tasksPagerAdapter
        TabLayoutMediator(tabsTabLayout, tasksViewPager) { tab, position ->
            tab.text = HomeTab.values()[position].title
        }.attach()
        tasksViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                dateRangeTextView.text = DateTimeUtils.getDateForTab(HomeTab.values()[position]) // todo: add animation
            }
        })
    }

    private fun setupObservers() {
    }

}
