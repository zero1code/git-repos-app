package br.com.mpsystems.cpmtracking.gitrepos.presentation.fragment.users

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import br.com.mpsystems.cpmtracking.gitrepos.R
import br.com.mpsystems.cpmtracking.gitrepos.databinding.FragmentReposBinding
import br.com.mpsystems.cpmtracking.gitrepos.databinding.FragmentUsersBinding
import br.com.mpsystems.cpmtracking.gitrepos.presentation.adapter.UserListAdapter
import br.com.mpsystems.cpmtracking.gitrepos.util.DividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsersFragment : Fragment(R.layout.fragment_users), SearchView.OnQueryTextListener {

    private var binding: FragmentUsersBinding? = null
    private val viewModel: UsersViewModel by viewModels()
    private val adapter by lazy { UserListAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.actionBar
        setHasOptionsMenu(true)

        binding = FragmentUsersBinding.bind(view)

        binding?.let {
            it.rvUsers.adapter = adapter
        }
        lifecycleScope.launchWhenStarted {
            viewModel.userList.collect { event ->
                when (event) {
                    UsersViewModel.UsersListResult.Empty -> {
                        Toast.makeText(requireContext(), "Vazio", Toast.LENGTH_SHORT).show()
                    }
                    is UsersViewModel.UsersListResult.Failure -> {
                        Toast.makeText(requireContext(), "Falhou", Toast.LENGTH_SHORT).show()
                    }
                    UsersViewModel.UsersListResult.Loading -> {
                        Toast.makeText(requireContext(), "Carregando", Toast.LENGTH_SHORT).show()
                    }
                    is UsersViewModel.UsersListResult.Success -> {
                        adapter.submitList(event.lista)
                    }
                }
            }
        }
        viewModel.getUsersSearched()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setOnQueryTextListener(this)
        searchView.queryHint = "Pesquisar..."

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
//        query?.let { viewModel.getRepoList(it) }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Log.e(TAG, "onQueryTextChange: $newText")
        return true
    }

    companion object {
        private const val TAG = "TAG"
    }
}