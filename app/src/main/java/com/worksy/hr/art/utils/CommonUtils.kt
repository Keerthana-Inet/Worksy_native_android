package com.worksy.hr.art.utils

import android.app.DatePickerDialog
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import com.worksy.hr.art.databinding.CreateClaimFormLayoutBinding
import com.worksy.hr.art.viewmodels.ClaimViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object CommonUtils {
    fun downloadImage(context: Context, url: String, fileName: String) {
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("Downloading Image")
            .setDescription("Downloading an image using DownloadManager.")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)

        Toast.makeText(context, "Downloading...", Toast.LENGTH_SHORT).show()
    }
}
object ShowDatePickerUtils{
    fun setCurrentDate(binding: CreateClaimFormLayoutBinding,viewModel: ClaimViewModel) {
        val calendar = Calendar.getInstance()
        val currentDate = formatDate(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR), "yyyy-MM-dd")
        binding.date.text = currentDate
        viewModel.transactionDate = currentDate
    }
    fun startDatePicker(context: Context, binding: CreateClaimFormLayoutBinding, viewModel: ClaimViewModel, isStartDate:Boolean) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Initialize the selected date to the current date
        var selectedDate = formatDate(day, month, year, "yyyy-MM-dd")

        val datePickerDialog = DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                selectedDate = formatDate(selectedDayOfMonth, selectedMonth, selectedYear, "yyyy-MM-dd")

                if (isStartDate) {
                    binding.date.text = selectedDate
                    viewModel.transactionDate = selectedDate
                } else {
                    val endDate = formatDate(selectedDayOfMonth, selectedMonth, selectedYear, "dd MMM, yyyy")
                    val startDateString = binding.date.text.toString()
                    if (startDateString.isNotEmpty()) {
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val startDate = Calendar.getInstance()
                        startDate.time = dateFormat.parse(startDateString)

                        val endDateCalendar = Calendar.getInstance()
                        endDateCalendar.time = dateFormat.parse(endDate)
                    } else {
                        Toast.makeText(context, "Please select a date", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            year,
            month,
            day
        )

        // Set the initial date in the date picker to the current date
        binding.date.text = selectedDate
        viewModel.transactionDate = selectedDate

        datePickerDialog.show()
    }
    // Utility function to format date
    private fun formatDate(day: Int, month: Int, year: Int, pattern: String): String {
        val calendar = Calendar.getInstance().apply {
            set(year, month, day)
        }
        val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}