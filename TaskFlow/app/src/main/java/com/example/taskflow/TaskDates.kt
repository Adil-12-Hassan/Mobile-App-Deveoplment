package com.example.taskflow

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/** Dates are stored as yyyy-MM-dd so that alphabetical order equals date order. */
object TaskDates {
    private val isoFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val displayFormat = SimpleDateFormat("dd MMM yyyy", Locale.US)
    fun toIso(year: Int, month: Int, day: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return isoFormat.format(calendar.time)
    }

    fun toDisplay(iso: String): String {
        val date = runCatching { isoFormat.parse(iso) }.getOrNull() ?: return iso
        return displayFormat.format(date)
    }

    fun isOverdue(iso: String): Boolean {
        val todayIso = isoFormat.format(Calendar.getInstance().time)
        return iso < todayIso
    }
}