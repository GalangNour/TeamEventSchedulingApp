package com.applandeo.calendarsampleapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.applandeo.calendarsampleapp.databinding.ActivityMainBinding
import com.applandeo.calendarsampleapp.extensions.getDot
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.builders.DatePickerBuilder
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener
import com.example.myapplication.AddNoteActivity
import com.example.myapplication.NotePreviewActivity
import java.io.Serializable
import java.util.Calendar

class MainActivity : AppCompatActivity(), OnDayClickListener {

    private lateinit var binding: ActivityMainBinding

    private val notes = mutableMapOf<EventDay, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabButton.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }

        binding.calendarView.setOnDayClickListener(this)
        binding.calendarView.setCalendarDayLayout(R.layout.activity_custom_calendar_day_row)
    }

//    private fun openDatePicker() {
//        DatePickerBuilder(this, this)
//            .pickerType(CalendarView.RANGE_PICKER)
//            .headerColor(R.color.primary)
//            .todayLabelColor(R.color.secondary)
//            .selectionColor(R.color.secondary_light)
//            .dialogButtonsColor(R.color.secondary)
//            .build()
//            .show()
//    }

    override fun onDayClick(eventDay: EventDay) {
        val intent = Intent(this, NotePreviewActivity::class.java)
        intent.putExtra(CALENDAR_EXTRA, eventDay.calendar)
        intent.putExtra(NOTE_EXTRA, notes[eventDay])
        startActivity(intent)
    }

//    override fun onSelect(calendar: List<Calendar>) {
//        val intent = Intent(this, AddNoteActivity::class.java)
//
//        // Use CalendarUtils.getDatesRange to get the full date range
//        val firstDay = calendar[0]
//        val lastDay = calendar[calendar.size - 1]
//        val dateRange = CalendarUtils.getDatesRange(firstDay, lastDay) // Use CalendarUtils function
//
//        // Pass the date range to the AddNoteActivity
//        intent.putExtra(CALENDAR_EXTRA, dateRange as Serializable) // Explicitly cast to Serializable
//        startActivityForResult(intent, RESULT_CODE)
//    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == RESULT_CODE) {
            val note = data?.getStringExtra(NOTE_EXTRA) ?: return
            val dateRange = data?.getSerializableExtra(CALENDAR_EXTRA) as List<Calendar>

            for (calendar in dateRange) {
                val eventDay = EventDay(calendar, applicationContext.getDot())
                notes[eventDay] = note
            }
            binding.calendarView.setEvents(notes.keys.toList())
            binding.calendarView.setHighlightedDays(dateRange)
        }
    }

    companion object {
        const val CALENDAR_EXTRA = "calendar"
        const val NOTE_EXTRA = "note"
        const val RESULT_CODE = 8
    }
}
