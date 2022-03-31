package br.com.mpsystems.cpmtracking.gitrepos.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import br.com.mpsystems.cpmtracking.gitrepos.presentation.fragment.favorites.FavoritesFragment
import br.com.mpsystems.cpmtracking.gitrepos.presentation.fragment.repos.ReposFragment
import br.com.mpsystems.cpmtracking.gitrepos.presentation.fragment.users.UsersFragment

class TabViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    val tabs = arrayOf("REPOSITÓRIOS", "USUÁRIOS", "FAVORITOS")
    private val fragments = arrayOf(ReposFragment(), UsersFragment(), FavoritesFragment())

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position] as Fragment
    }

}