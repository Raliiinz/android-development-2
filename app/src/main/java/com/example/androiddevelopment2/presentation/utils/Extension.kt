package com.example.androiddevelopment2.presentation.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment


fun Fragment.hideKeyboard() {
    val activity = requireActivity()
    val view = activity.currentFocus ?: View(activity)
    val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun String.formatRecipeInstructions(): String {
    var text = this
    text = text.replace(Regex("<li>", RegexOption.IGNORE_CASE), "\n• ")
        .replace(Regex("</li>", RegexOption.IGNORE_CASE), "")
    text = text.replace(Regex("<ol>", RegexOption.IGNORE_CASE), "")
        .replace(Regex("</ol>", RegexOption.IGNORE_CASE), "")
    text = text.replace(Regex("<(?!br\\s*/?>)[^>]+>", RegexOption.IGNORE_CASE), "")
    text = text.replace(Regex("<br\\s*/?>", RegexOption.IGNORE_CASE), "\n")
    text = text.replace(Regex("\\s*\n\\s*"), "\n")
        .replace(Regex(" {2,}"), " ")
        .replace(Regex("\n{3,}"), "\n\n")
    return text.trim()
}
