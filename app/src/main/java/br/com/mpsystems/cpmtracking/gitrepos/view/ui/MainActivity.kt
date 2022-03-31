package br.com.mpsystems.cpmtracking.gitrepos.view.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.mpsystems.cpmtracking.gitrepos.databinding.ActivityMainBinding
import br.com.mpsystems.cpmtracking.gitrepos.view.adapter.TabViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        setupViews()

    }

    private fun setupViews() {
        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager2
        val adapter = TabViewPagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = adapter.tabs[position]
        }.attach()


    }
}