package com.waqar.tmdb.utils

import com.waqar.tmdb.network.Resource
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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

