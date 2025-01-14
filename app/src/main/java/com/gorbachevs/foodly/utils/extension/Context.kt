package com.gorbachevs.foodly.utils.extension

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.widget.Toast
import androidx.annotation.StringRes
import com.gorbachevs.foodly.domain.model.Recipe

fun Context.shareRecipe(uri: Uri, recipe: Recipe) {
    val shareCardIntent = Intent().apply {
        action = Intent.ACTION_SEND
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, buildRecipeText(recipe))
        putExtra(Intent.EXTRA_STREAM, uri)
        clipData = ClipData.newRawUri(null, uri)
        flags += Intent.FLAG_GRANT_READ_URI_PERMISSION
    }
    val shareIntent = Intent.createChooser(shareCardIntent, "Share recipe")
    startActivity(shareIntent)
}

fun buildRecipeText(recipe: Recipe): String {
    return buildString {
        append("Recipe: ${recipe.name}\n\n")
        append("Ingredients:\n")
        recipe.ingredients.forEach { ingredient ->
            append("- $ingredient\n")
        }
        append("\nInstructions:\n")
        recipe.instructions.forEach { step ->
            append("- $step\n")
        }
    }
}

fun Context.showError(error: Throwable) {
    Toast.makeText(this, error.message.toString(), Toast.LENGTH_SHORT).show()
}

fun Context.showMessage(@StringRes messageResId: Int) {
    Toast.makeText(this, getString(messageResId), Toast.LENGTH_SHORT).show()
}

fun Context.showMessage(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.hasNetwork(): Boolean {
    var isConnected = false
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.run {
        isConnected = when {
            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
    return isConnected
}
