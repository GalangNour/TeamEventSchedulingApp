package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.applandeo.calendarsampleapp.MainActivity
import com.applandeo.calendarsampleapp.MainActivity.Companion.CALENDAR_EXTRA
import com.applandeo.calendarsampleapp.MainActivity.Companion.NOTE_EXTRA
import com.applandeo.calendarsampleapp.R
import com.applandeo.calendarsampleapp.databinding.ActivityAddNoteBinding
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.builders.DatePickerBuilder
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener
import com.applandeo.materialcalendarview.utils.calendar
import java.text.SimpleDateFormat
import java.util.*

class AddNoteActivity : AppCompatActivity(), OnSelectDateListener {

    private lateinit var binding: ActivityAddNoteBinding
    private val selectedCalendars = mutableListOf<Calendar>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pickDateButton.setOnClickListener {
            openDatePicker()
        }

        binding.saveButton.setOnClickListener {
            val eventName = binding.eventNameEditText.text.toString()
            if (eventName.isNotEmpty() && selectedCalendars.isNotEmpty()) {
                val returnIntent = Intent()

                returnIntent.putExtra(CALENDAR_EXTRA, ArrayList(selectedCalendars))
                returnIntent.putExtra(NOTE_EXTRA, eventName)
                setResult(RESULT_OK, returnIntent)
            }
            finish()
        }
    }

    override fun onSelect(selectedCalendars: List<Calendar>) {
        this.selectedCalendars.addAll(selectedCalendars)
        updateDateRangeTextView()
    }

    private fun openDatePicker() {
        DatePickerBuilder(this, this)
            .pickerType(CalendarView.RANGE_PICKER)
            .headerColor(R.color.primary)
            .todayLabelColor(R.color.secondary)
            .selectionColor(R.color.secondary_light)
            .dialogButtonsColor(R.color.secondary)
            .build()
            .show()
    }

    private fun updateDateRangeTextView() {
        if (selectedCalendars.size == 2) {
            val startDate = selectedCalendars[0]
            val endDate = selectedCalendars[1]

            val startDateStr = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(startDate.time)
            val endDateStr = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(endDate.time)

            // Set the formatted dates to TextViews
            binding.startDateTextView.text = startDateStr
            binding.endDateTextView.text = endDateStr
        }
    }

    // Additional functions for setting min, max, disabled, and highlighted dates can be added as needed.
}