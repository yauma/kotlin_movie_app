package com.example.jaimequeraltgarrigos.kotlinmovieapp.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import com.example.jaimequeraltgarrigos.kotlinmovieapp.R
import com.example.jaimequeraltgarrigos.kotlinmovieapp.ui.adapter.MovieAdapter
import com.example.jaimequeraltgarrigos.kotlinmovieapp.ui.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_fragment.*

@AndroidEntryPoint
class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.onMainViewCreated()
        val adapter = MovieAdapter()
        recyclerView.adapter = adapter
        subscribeUi(adapter)

        setHasOptionsMenu(true)

        // Show a snackbar whenever the [ViewModel.snackbar] is updated a non-null value
        viewModel.snackbar.observe(this) { text ->
            text?.let {
                Snackbar.make(main, text, Snackbar.LENGTH_SHORT).show()
                viewModel.onSnackbarShown()
            }
        }

        // show the spinner when [MainViewModel.spinner] is true
        viewModel.spinner.observe(this) { value ->
            value.let { show ->
                progress_bar.visibility = if (show) View.VISIBLE else View.GONE
            }
        }
    }

    private fun subscribeUi(adapter: MovieAdapter) {
        viewModel.movies.observe(this) { movies ->
            adapter.submitList(movies)
        }
    }
}