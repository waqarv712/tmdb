package com.waqar.tmdb.utils

import com.waqar.tmdb.network.Resource
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.waqar.tmdb.R

inline fun <reified T : Any> Context.launchActivity(
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {

    val intent = newIntent<T>(this)
    intent.init()
    startActivity(intent, options)
}

inline fun <reified T : Any> newIntent(context: Context): Intent =
    Intent(context, T::class.java)

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Activity.handleApiErrorWithToast(
    failure: Resource.Failure,
    retry: (() -> Unit)? = null
) {

    when {
        failure.isNetworkError -> toast(
            getString(R.string.please_check_your_internet_connection)
        )
        else -> {
//            val error = failure.errorBody?.string().toString()
            val error = getString(R.string.someting_went_wrong)
            toast(error)
        }
    }
}

@BindingAdapter(value = ["setImageUrl"])
fun ImageView.loadImage(imageUrl: String?) {

    this.post {

        val myOptions = RequestOptions()
            .override(this.width, this.height)

        Glide.with(this.context.applicationContext)
            .load(imageUrl)
            .timeout(60000)
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_placeholder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .skipMemoryCache(false)
            .apply(myOptions)
            .dontAnimate()
            .into(this)


    }

}

fun View.setFadeAnimation() {
    val anim = AlphaAnimation(0.0f, 1.0f)
    anim.duration = 500
    startAnimation(anim)
}

fun Fragment.handleApiErrorWithToast(
    failure: Resource.Failure,
    retry: (() -> Unit)? = null
) {

    when {
        failure.isNetworkError -> toast(
            getString(R.string.please_check_your_internet_connection)
        )
//        failure.errorCode == 401 -> {
//            if (this is HomeFragment) {
//                toast("Something goes wrong")
//            } else {
//                //@TODO perform some functionality
//            }
//        }
        else -> {
//            val error = failure.errorBody?.string().toString()
            val error = getString(R.string.someting_went_wrong)
            toast(error)
        }
    }
}