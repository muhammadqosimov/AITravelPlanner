package com.example.aitravelplanner.util

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight

@Composable
fun MarkdownText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified
) {
    Text(
        text = parseBasicMarkdown(text),
        modifier = modifier,
        style = style,
        color = color
    )
}

fun parseBasicMarkdown(text: String): AnnotatedString = buildAnnotatedString {
    val lines = text.lines()
    lines.forEachIndexed { index, line ->
        val headingContent = when {
            line.startsWith("### ") -> line.removePrefix("### ")
            line.startsWith("## ") -> line.removePrefix("## ")
            line.startsWith("# ") -> line.removePrefix("# ")
            else -> null
        }
        when {
            headingContent != null -> {
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                appendInlineBold(headingContent)
                pop()
            }
            line.trimStart() == "---" -> append("─────────────────────")
            else -> appendInlineBold(line)
        }
        if (index < lines.lastIndex) append("\n")
    }
}

private fun AnnotatedString.Builder.appendInlineBold(text: String) {
    val boldRegex = Regex("""\*\*(.+?)\*\*""")
    var lastEnd = 0
    boldRegex.findAll(text).forEach { match ->
        append(text.substring(lastEnd, match.range.first))
        pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
        append(match.groupValues[1])
        pop()
        lastEnd = match.range.last + 1
    }
    append(text.substring(lastEnd))
}
