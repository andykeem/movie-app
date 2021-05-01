package com.example.movieapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.MovieApp
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentFavoriteBinding
import com.example.movieapp.db.entity.Favorite
import com.example.movieapp.viewmodel.FavoriteViewModel
import com.example.movieapp.viewmodel.MovieViewModel.Companion.EXTRA_MOVIE
import com.google.gson.Gson

class FavoriteFragment : Fragment() {

    lateinit var binding: FragmentFavoriteBinding
    lateinit var viewModel: FavoriteViewModel
    lateinit var favoriteListAdapter: FavoriteListAdapter
    val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = (context?.applicationContext as MovieApp).db
        val factory = FavoriteViewModel.Factory(db, gson)
        viewModel = ViewModelProvider(this, factory).get(FavoriteViewModel::class.java)
        viewModel.favoriteList.observe(this) {
            it?.let { list ->
                updateList(list)
            }
        }
        viewModel.getFavoriteList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false)
        binding.model = viewModel
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        favoriteListAdapter = FavoriteListAdapter(context)
        binding.recyclerView.adapter = favoriteListAdapter
        return binding.root
    }

    private fun updateList(list: List<Favorite>) {
        list.forEach {
            favoriteListAdapter.list.add(it)
        }
        favoriteListAdapter.notifyDataSetChanged()
    }

    inner class FavoriteListAdapter(val ctx: Context?) : RecyclerView.Adapter<FavoriteViewHolder>() {
        val list = mutableListOf<Favorite>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
            val view = LayoutInflater.from(ctx)
                    .inflate(android.R.layout.simple_list_item_1, parent, false)
            return FavoriteViewHolder(view)
        }

        override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
            val item = list[position]
            holder.bindData(item)
        }

        override fun getItemCount() = list.size
    }

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val textView: TextView = itemView.findViewById(android.R.id.text1)
        lateinit var item: Favorite

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            with (Intent(v?.context, MovieDetailActivity::class.java)) {
                val movie = viewModel.getMovieByFavorite(item)
                putExtra(EXTRA_MOVIE, movie)
                startActivity(this)
            }
        }

        fun bindData(item: Favorite) {
            this.item = item
            textView.text = item.movieTitle
        }
    }

    companion object {

        fun newInstance(): Fragment {
            return FavoriteFragment()
        }
    }
}