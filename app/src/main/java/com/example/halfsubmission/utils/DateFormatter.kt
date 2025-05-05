package com.example.halfsubmission.utils

import java.text.SimpleDateFormat
import java.util.Locale

object DateFormatter {
     fun formatDate(dateString: String): String {
        // Logging input dateString
        return try {
            val inputFormat =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()) // Format input
            val outputFormat = SimpleDateFormat(
                "HH:mm a . EEEE, d MMM  yyyy",
                Locale("id", "ID")
            ) // Format output dalam Bahasa Indonesia
            val date = inputFormat.parse(dateString)

            date?.let {
                // Replace nama hari ke format lokal
                val formattedDate = outputFormat.format(it)
                formattedDate.replace("Monday", "Senin")
                    .replace("Tuesday", "Selasa")
                    .replace("Wednesday", "Rabu")
                    .replace("Thursday", "Kamis")
                    .replace("Friday", "Jumat")
                    .replace("Saturday", "Sabtu")
                    .replace("Sunday", "Minggu")
            } ?: dateString // Kembalikan string asli jika parsing gagal
        } catch (e: Exception) {
            dateString // Kembalikan string asli jika terjadi kesalahan
        }
    }
}
