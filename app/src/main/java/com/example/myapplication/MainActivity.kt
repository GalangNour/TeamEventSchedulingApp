package com.applandeo.calendarsampleapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.applandeo.calendarsampleapp.databinding.ActivityMainBinding
import com.applandeo.calendarsampleapp.extensions.*
import com.applandeo.calendarsampleapp.extensions.getDot
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.getDatesRange
import com.applandeo.materialcalendarview.builders.DatePickerBuilder
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener

import com.example.myapplication.AddNoteActivity
import com.example.myapplication.EventNoteViewModel
import com.example.myapplication.NotePreviewActivity
import java.io.Serializable
import java.util.Calendar

class MainActivity : AppCompatActivity(), OnDayClickListener {

    private lateinit var binding: ActivityMainBinding

    private val notes = mutableMapOf<EventDay, String>()
    private lateinit var viewModelEventNote : EventNoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModelEventNote = ViewModelProvider(this).get(EventNoteViewModel::class.java)

        binding.fabButton.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }

        binding.calendarView.setOnDayClickListener(this)
        binding.calendarView.setCalendarDayLayout(R.layout.activity_custom_calendar_day_row)

        handleActivityResult(null)

    }

    override fun onDayClick(eventDay: EventDay) {
        val intent = Intent(this, NotePreviewActivity::class.java)
        intent.putExtra(CALENDAR_EXTRA, eventDay.calendar)
        intent.putExtra(NOTE_EXTRA, notes[eventDay])
        startActivity(intent)
    }


// ...

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == RESULT_CODE) {
            handleActivityResult(data)
        }
    }
    private fun handleActivityResult(data: Intent?) {
        val note = data?.getStringExtra(NOTE_EXTRA) ?: return

        // Declare dateRange outside of the callback scope
        var dateRange: List<Calendar>? = null

        // Fetch EventNotes from the database
        viewModelEventNote.getAllEventNotes(
            onDataReceived = { eventNotes ->
                val dateRangeList = mutableListOf<Pair<Calendar, Calendar>>()

                // Iterate through the retrieved EventNotes
                for (eventNote in eventNotes) {
                    // Convert date_start and date_end to Calendar instances
                    val startDate = Calendar.getInstance()
                    startDate.time = eventNote.date_start
                    val endDate = Calendar.getInstance()
                    endDate.time = eventNote.date_end

                    // Add the pair of start and end dates to the list
                    dateRangeList.add(startDate to endDate)
                }

                // Process dateRangeList as needed
                // ...

                // Set dateRange to the processed list
                dateRange = dateRangeList.map { it.first } // For example, use the start dates

                // Rest of your code to create dateRange and update the calendar
                binding.calendarView.setEvents(notes.keys.toList())

                // Check if dateRange is not null before using it
                dateRange?.let { range ->
                    binding.calendarView.setHighlightedDays(range)
                }
            },
            onError = { error ->
                // Handle the error here if needed
            }
        )

        // dateRange is accessible here but may still be null until the callback is invoked
    }

    companion object {
        const val CALENDAR_EXTRA = "calendar"
        const val NOTE_EXTRA = "note"
        const val RESULT_CODE = 8
    }
}
