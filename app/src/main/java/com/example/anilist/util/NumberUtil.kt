package com.example.anilist.util

import java.text.DecimalFormat
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

object NumberUtil {

    fun Int.prettyCount(): String {
        val suffix = charArrayOf('\u0000', 'k', 'M', 'B')
        val numValue = toLong()
        val value = floor(log10(numValue.toDouble())).toInt()
        val base = value / 3
        return if (value >= 3 && base < suffix.size) {
            DecimalFormat("#0.0").format(
                numValue / 10.0.pow((base * 3).toDouble())
            ) + suffix[base]
        } else {
            DecimalFormat("#,##0").format(numValue)
        }
    }

    fun Int?.orZero(): Int {
        return this ?: 0
    }

    fun Long?.orZero(): Long {
        return this ?: 0L
    }

    operator fun Boolean.plus(number: Int): Int {
        return number + if (this) 1 else 0
    }
}