package br.com.mpsystems.cpmtracking.gitrepos.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.mpsystems.cpmtracking.gitrepos.databinding.ActivityMainBinding
import br.com.mpsystems.cpmtracking.gitrepos.presentation.adapter.TabViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    var tabLayout: TabLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        setupViews()
    }

    private fun setupViews() {
//        if (position != 0) {
//            val badge = tab.orCreateBadge
//            badge.verticalOffset = 28
//            badge.horizontalOffset = -25
//        }
        tabLayout = binding.tabLayout
        val viewPager = binding.viewPager2
        val adapter = TabViewPagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout!!, viewPager) { tab, position ->
            tab.text = adapter.tabs[position]
        }.attach()

        tabsListeners()
    }

    private fun tabsListeners() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    1 -> tab.removeBadge()
                    2 -> tab.removeBadge()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }
}
