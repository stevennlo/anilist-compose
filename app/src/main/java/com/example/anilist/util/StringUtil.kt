package com.example.anilist.util

import android.graphics.Typeface
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.core.text.HtmlCompat

object StringUtil {

    fun String?.orHyphen(): String {
        return this ?: "-"
    }

    fun String.toAnnotatedString(): AnnotatedString {
        val spanned = HtmlCompat.fromHtml(this@toAnnotatedString, HtmlCompat.FROM_HTML_MODE_COMPACT)
        with(spanned) {
            return buildAnnotatedString {
                append(spanned.toString())
                getSpans(0, spanned.length, Any::class.java).forEach { span ->
                    val start = getSpanStart(span)
                    val end = getSpanEnd(span)
                    when (span) {
                        is StyleSpan -> when (span.style) {
                            Typeface.BOLD -> addStyle(
                                SpanStyle(fontWeight = FontWeight.Bold),
                                start,
                                end
                            )
                            Typeface.ITALIC -> addStyle(
                                SpanStyle(fontStyle = FontStyle.Italic),
                                start,
                                end
                            )
                            Typeface.BOLD_ITALIC -> addStyle(
                                SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontStyle = FontStyle.Italic
                                ), start, end
                            )
                        }
                        is UnderlineSpan -> addStyle(
                            SpanStyle(textDecoration = TextDecoration.Underline),
                            start,
                            end
                        )
                        is ForegroundColorSpan -> addStyle(
                            SpanStyle(color = Color(span.foregroundColor)),
                            start,
                            end
                        )
                    }
                }
            }
        }
    }

}