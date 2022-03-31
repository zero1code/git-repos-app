package br.com.mpsystems.cpmtracking.gitrepos.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import br.com.mpsystems.cpmtracking.gitrepos.view.ui.ReposFragment

class TabViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    val tabs = arrayOf("REPOSITÓRIOS", "USUÁRIOS")
    private val fragments = arrayOf(ReposFragment(), ReposFragment())

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

}