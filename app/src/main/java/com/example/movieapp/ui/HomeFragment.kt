package com.example.movieapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentHomeBinding
import com.example.movieapp.databinding.ListItemMovieBinding
import com.example.movieapp.model.Movie
import com.example.movieapp.network.MovieApi
import com.example.movieapp.network.MovieService
import com.example.movieapp.viewmodel.HomeViewModel
import com.example.movieapp.viewmodel.MovieViewModel
import com.google.gson.Gson
import java.lang.ref.WeakReference

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: HomeViewModel
    lateinit var nowPlayingAdapter: MovieHolderAdapter
    lateinit var mostPopularAdapter: MovieHolderAdapter
    val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val service = MovieApi.httpClient().create(MovieService::class.java)
        val factory = HomeViewModel.Factory(service)
        viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

        viewModel.nowPlaying.observe(this) {
            it?.let { list ->
                updateNowPlaying(list)
            }
        }
        viewModel.mostPopular.observe(this) {
            it?.let { list ->
                updateMostPopular(list)
            }
        }

        viewModel.fetchNowPlaying()
        viewModel.fetchMostPoular()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home,
            container, false)
        binding.model = viewModel

        context?.let {
            nowPlayingAdapter = MovieHolderAdapter(it, R.layout.list_item_movie)
            binding.carouselNowPlaying.layoutManager = LinearLayoutManager(it,
                LinearLayoutManager.HORIZONTAL, false)
            binding.carouselNowPlaying.adapter = nowPlayingAdapter

            mostPopularAdapter = MovieHolderAdapter(it, R.layout.list_item_movie)
            binding.carouselMostPopular.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false)
            binding.carouselMostPopular.adapter = mostPopularAdapter
        }

        return binding.root
    }

    private fun updateNowPlaying(list: List<Movie>) {
        list.forEach {
            it.json = gson.toJson(it, Movie::class.java)
            nowPlayingAdapter.list.add(it)
        }
        nowPlayingAdapter.notifyDataSetChanged()
    }

    private fun updateMostPopular(list: List<Movie>) {
        list.forEach {
            it.json = gson.toJson(it, Movie::class.java)
            mostPopularAdapter.list.add(it)
        }
        mostPopularAdapter.notifyDataSetChanged()
    }

    inner class MovieHolderAdapter(ctx: Context,
                                 val resource: Int) : RecyclerView.Adapter<MovieHolder>() {
        var weakCtx = WeakReference(ctx)
        var list = mutableListOf<Movie>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
            val inflater = LayoutInflater.from(weakCtx.get())
            val binding: ListItemMovieBinding = DataBindingUtil.inflate(inflater, resource, parent, false)
            return MovieHolder(binding)
        }

        override fun onBindViewHolder(holder: MovieHolder, position: Int) {
            val data = list[position]
            holder.onBindData(data)
        }

        override fun getItemCount() = list.size
    }

    inner class MovieHolder(val binding: ListItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBindData(movie: Movie) {
            binding.model = MovieViewModel(movie)
            binding.notifyChange()
        }
    }

    companion object {
        fun newInstance(): Fragment {
            return HomeFragment()
        }
    }
}