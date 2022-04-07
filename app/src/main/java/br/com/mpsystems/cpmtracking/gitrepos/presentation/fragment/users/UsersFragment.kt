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
import kotlinx.coroutines.Job

@AndroidEntryPoint
class UsersFragment : Fragment(R.layout.fragment_users) {

    private var binding: FragmentUsersBinding? = null
    private val viewModel: UsersViewModel by viewModels()
    private val adapter by lazy { UserListAdapter() }
    private var userStateJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentUsersBinding.bind(view)

        binding?.let {
            it.rvUsers.adapter = adapter
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUsersSearched()
    }

    override fun onStart() {
        super.onStart()
        setupLifecycle()
    }

    override fun onStop() {
        userStateJob?.cancel()
        super.onStop()
    }

    private fun setupLifecycle() {
        userStateJob = lifecycleScope.launchWhenStarted {
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
    }
}