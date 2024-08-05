package com.worksy.hr.art.views.fragments.travelRequest

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.FragmentTravelRequestTab2Binding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class TravelRequestTab2 : Fragment(R.layout.fragment_travel_request_tab2) {

    private var _binding: FragmentTravelRequestTab2Binding?=null
    private val binding get() = _binding!!
    private lateinit var context: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTravelRequestTab2Binding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        // unbind view onDestroyView
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context=this.requireContext()
        binding.apply {
            dateStart.setOnClickListener {
                startDatePicker(binding,true)
            }
            dateEnd.setOnClickListener {
                startDatePicker(binding,false)
            }
        }
    }

    private fun startDatePicker(binding: FragmentTravelRequestTab2Binding, isStartDate: Boolean) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                val selectedDate = formatDate(selectedDayOfMonth, selectedMonth, selectedYear, "dd MMM, yyyy")

                if (isStartDate) {
                    binding.dateStart.text = selectedDate
                } else {
                    val endDate = formatDate(selectedDayOfMonth, selectedMonth, selectedYear, "dd MMM, yyyy")

                    val startDateString = binding.dateStart.text.toString()
                    if (startDateString.isNotEmpty()) {
                        val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
                        val startDate = Calendar.getInstance()
                        startDate.time = dateFormat.parse(startDateString)

                        val endDateCalendar = Calendar.getInstance()
                        endDateCalendar.time = dateFormat.parse(endDate)

                        if (endDateCalendar >= startDate) {
                            binding.dateEnd.text = selectedDate
                        } else {
                            Toast.makeText(requireActivity(), "End date must be greater than or equal to start date", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(requireActivity(), "Please select a start date first", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            year,
            month,
            day
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun formatDate(day: Int, month: Int, year: Int, format: String): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

}