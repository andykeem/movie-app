package com.example.movieapp.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object DataBinding {

    @JvmStatic
    @BindingAdapter(value = ["imgUrl", "placeholder"], requireAll = false)
    fun loadImage(view: ImageView, url: String?, placeholder: Drawable) {
        url?.let {
            Glide.with(view.context.applicationContext)
                    .load(url)
                    .placeholder(placeholder)
                    .fallback(placeholder)
                    .into(view)
        } ?: run {
            view.setImageDrawable(placeholder)
        }
    }
}