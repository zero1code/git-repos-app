package br.com.mpsystems.cpmtracking.gitrepos.presentation.fragment.repos

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import br.com.mpsystems.cpmtracking.gitrepos.R
import br.com.mpsystems.cpmtracking.gitrepos.databinding.FragmentReposBinding
import br.com.mpsystems.cpmtracking.gitrepos.domain.model.Repo
import br.com.mpsystems.cpmtracking.gitrepos.presentation.adapter.RepoListAdapter
import br.com.mpsystems.cpmtracking.gitrepos.presentation.fragment.favorites.FavoritesViewModel
import br.com.mpsystems.cpmtracking.gitrepos.presentation.ui.MainActivity
import br.com.mpsystems.cpmtracking.gitrepos.util.*
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job

@AndroidEntryPoint
class ReposFragment : Fragment(R.layout.fragment_repos), SearchView.OnQueryTextListener {

    private var binding: FragmentReposBinding? = null
    private val viewModel: ReposViewModel by viewModels()
    private val viewModelFavorite: FavoritesViewModel by viewModels()
    private lateinit var repos: List<Repo>
    private val adapter by lazy { RepoListAdapter() }
    private val dialog by lazy { activity?.createProgressDialog() }
    private var reposStateJob: Job? = null
    private var lastUser: String = ""

    private var tableLayout: TabLayout? = null
    private var ivChangeTheme: ImageView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.actionBar
        setHasOptionsMenu(true)

        binding = FragmentReposBinding.bind(view)

        binding?.let { mBinding ->
            mBinding.rvRepos.adapter = adapter
            ViewCompat.setNestedScrollingEnabled(mBinding.rvRepos, false)
            mBinding.rvRepos.addItemDecoration(DividerItemDecoration(requireContext()))
        }

        tableLayout = (activity as? MainActivity)?.tabLayout
        ivChangeTheme = (activity as? MainActivity)?.ivChangeTheme



        insertListeners()
    }

    override fun onResume() {
        super.onResume()
        if (lastUser.isNotEmpty()) viewModel.getRepoList(lastUser)
    }

    override fun onStart() {
        super.onStart()
        setupLifecycle()
    }

    override fun onStop() {
        reposStateJob?.cancel()
        super.onStop()
    }

    private fun setupLifecycle() {
        reposStateJob = lifecycleScope.launchWhenStarted {
            viewModel.repoList.collect { event ->
                when (event) {
                    ReposViewModel.RepoApiResult.Empty -> {
                        dialog?.dismiss()
                        binding?.layoutEmptyState?.main?.visibility = View.VISIBLE
                    }
                    is ReposViewModel.RepoApiResult.Failure -> {
                        dialog?.dismiss()
                        if (event.errorText.isNotEmpty()) {
                            binding?.layoutFailureState?.textView?.text = event.errorText
                        }
                        binding?.layoutFailureState?.main?.visibility = View.VISIBLE
                    }
                    ReposViewModel.RepoApiResult.Loading -> {
                        dialog?.show()
                    }
                    is ReposViewModel.RepoApiResult.Success -> {
                        repos = event.lista
                        adapter.submitList(repos)
                        viewModel.insertUser(event.lista[0].owner)

                        binding?.let {
                            it.tvUser.text = event.lista[0].owner.login
                            Glide.with(requireContext()).load(event.lista[0].owner.avatarURL)
                                .into(it.ivUser)
                        }
                        dialog?.dismiss()
                        binding?.layoutEmptyState?.main?.visibility = View.GONE
                        binding?.layoutFailureState?.main?.visibility = View.GONE
                    }
                    is ReposViewModel.RepoApiResult.SuccessUser -> {
                        val badge = tableLayout?.getTabAt(1)?.orCreateBadge
                        badge?.number = 1


                        viewModel.nothingToDo()
                    }
                    is ReposViewModel.RepoApiResult.SuccessFavorite -> {
                        val badge = tableLayout?.getTabAt(2)?.orCreateBadge

                        viewModel.nothingToDo()
                    }
                    is ReposViewModel.RepoApiResult.Nothing -> Unit
                }
            }
        }
    }


    @SuppressLint("ResourceType")
    private fun insertListeners() {
        adapter.listenerFavorite = { position ->
            val repo = adapter.currentList[position]

            if (repo.isFavorite == 1) {
                viewModelFavorite.deleteFavorite(repo)
                repos.find { it.id == repo.id }?.isFavorite = 0
                binding?.root?.showSnackBar("Favorito removido.", resources.getString(R.color.red))
                    ?.show()
            } else {
                viewModel.insertFavorite(repo)
                repos.find { it.id == repo.id }?.isFavorite = 1
                binding?.root?.showSnackBar(
                    "Favorito adicionado.",
                    resources.getString(R.color.green)
                )?.show()
            }

            adapter.notifyItemChanged(position)
        }
    }

    fun takeScreenshotOfView(view: View, height: Int, width: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return bitmap
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setOnQueryTextListener(this)
        searchView.queryHint = "Pesquisar..."

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        binding?.root?.hideSoftKeyboard()
        query?.let {
            viewModel.getRepoList(it)
            lastUser = it
        }
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