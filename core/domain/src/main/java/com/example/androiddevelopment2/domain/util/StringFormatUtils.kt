package com.example.androiddevelopment2.domain.util

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
