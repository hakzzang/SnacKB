package hbs.com.snackb.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import hbs.com.snackb.R

@BindingAdapter(value = ["onBankImage"])
fun ImageView.setImageWithGlide(bankImage: String) {
    Glide.with(this)
        .load(bankImage)
        .placeholder(R.drawable.placeholder)
        .into(this)
}