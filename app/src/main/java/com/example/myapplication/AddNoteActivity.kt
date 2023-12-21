package com.example.myapplication

import EventNote
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
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
    private lateinit var viewModelEventNote : EventNoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModelEventNote = ViewModelProvider(this).get(EventNoteViewModel::class.java)

        binding.pickDateButton.setOnClickListener {
            openDatePicker()
        }

        binding.saveButton.setOnClickListener {
//            val eventName = binding.eventNameEditText.text.toString()
//            if (eventName.isNotEmpty() && selectedCalendars.isNotEmpty()) {
//                val returnIntent = Intent()
//
//                returnIntent.putExtra(CALENDAR_EXTRA, ArrayList(selectedCalendars))
//                returnIntent.putExtra(NOTE_EXTRA, eventName)
//                setResult(RESULT_OK, returnIntent)
//            }
            saveEvent()
        }
    }

    override fun onSelect(selectedCalendars: List<Calendar>) {
        this.selectedCalendars.clear()
        this.selectedCalendars.addAll(selectedCalendars)
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

    //TODO startDate and endDate taking same value
    private fun updateDateRangeTextView() : Pair<Calendar, Calendar>? {
        if (selectedCalendars.size == 2) {
            val startDate = selectedCalendars[0]
            val endDate = selectedCalendars[1]

            // Ensure that you are using the selected dates from the CalendarView
            // startDate and endDate should be correctly selected dates
            val startDateStr = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(startDate.time)
            val endDateStr = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(endDate.time)

            // Set the formatted dates to TextViews
            binding.startDateTextView.text = startDateStr
            binding.endDateTextView.text = endDateStr

            return Pair(startDate, endDate)
        }
        return null
    }

    fun saveEvent() {
        val dateRange = selectedCalendars.toList()
        val eventName = binding.eventNameEditText.text.toString()
        val eventDescription = binding.eventDescriptionEditText.text.toString()

        // Create EventNote object with the selected date range
        val newEventNote = EventNote(
            nama = eventName,
            deskripsi = eventDescription,
            team = null,
            date_start = dateRange.first().time, // Start date
            date_end = dateRange.last().time    // End date
        )

        viewModelEventNote.addEvent(newEventNote) { isSuccess ->
            if (isSuccess) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // Handle the case where the event was not added successfully
            }
        }
    }
}