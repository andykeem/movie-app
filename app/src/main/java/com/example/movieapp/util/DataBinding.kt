package com.example.movieapp.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object DataBinding {

    @JvmStatic
    @BindingAdapter(value = ["imgUrl"])
    fun loadImage(view: ImageView, url: String) {
        Glide.with(view.context.applicationContext)
            .load(url)
            .into(view)
    }
}