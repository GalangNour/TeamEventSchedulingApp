package com.applandeo.calendarsampleapp

import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.applandeo.calendarsampleapp.databinding.ActivityMainBinding
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import com.example.myapplication.AddNoteActivity
import com.example.myapplication.EventNoteViewModel
import com.example.myapplication.NotePreviewActivity
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

        handleActivityResult()

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
            handleActivityResult()
        }
    }
    private fun handleActivityResult() {

        // Declare dateRange outside of the callback scope
        var dateRange: List<Calendar>?
        val defaultColor = Color.GREEN

        // Fetch EventNotes from the database
        viewModelEventNote.getAllEventNotes(
            onDataReceived = { eventNotes ->
                val events = mutableListOf<EventDay>()
                val dateRangeList = mutableListOf<Pair<Calendar, Calendar>>()

                // Iterate through the retrieved EventNotes
                for (eventNote in eventNotes) {

                    // Convert date_start and date_end to Calendar instances
                    val startDate = Calendar.getInstance().apply {
                        time = eventNote.date_start
                    }

                    val endDate = Calendar.getInstance().apply {
                        time = eventNote.date_end
                    }
                    val colorHex = eventNote.team
                    val color = try {
                        Color.parseColor(colorHex)
                    } catch (e: IllegalArgumentException) {
                        // Handle the case when the colorHex is not a valid color format
                        // You can set a default color or take appropriate action
                        Color.BLACK // Default color
                    }
                    val title = eventNote.nama


                    val datesInRange = mutableListOf<Calendar>()
                    var currentDate = startDate.clone() as Calendar

                    while (currentDate <= endDate) {
                        datesInRange.add(currentDate.clone() as Calendar)
                        currentDate.add(Calendar.DATE, 1)
                    }

                    for (date in datesInRange) {
                        // Use CalendarUtils.getDrawableText to create a Drawable with text
                        val textDrawable = getDrawableText(
                            title, // Use the title (eventNote.nama) as text
                            Typeface.DEFAULT, // You can customize the typeface if needed
                            defaultColor, // Use the color obtained from eventNote.team
                            20f // Specify the text size
                        )

                        events.add(EventDay(date, textDrawable)) // Add the Drawable with text for each date in the range
                    }                    // Add the pair of start and end dates to the list
                    dateRangeList.add(startDate to endDate)
                }


                // Process dateRangeList as needed
                // ...

                // Set dateRange to the processed list of date pairs (start and end dates)
                dateRange = dateRangeList.flatMap { datePair ->
                    val startDate = datePair.first
                    val endDate = datePair.second
                    val datesInRange = mutableListOf<Calendar>()

                    var currentDate = startDate.clone() as Calendar
                    while (currentDate <= endDate) {
                        datesInRange.add(currentDate.clone() as Calendar)
                        currentDate.add(Calendar.DATE, 1)
                    }

                    datesInRange
                }

                // Rest of your code to create dateRange and update the calendar

                // Check if dateRange is not null before using it
                dateRange?.let { range ->
                    binding.calendarView.setHighlightedDays(range)
                    binding.calendarView.setEvents(events)
//                    binding.calendarView.setEvents(notes.keys.toList())
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
    fun getDrawableText(text: String, typeface: Typeface, textColor: Int, textSize: Float): Drawable {
        val paint = Paint().apply {
            this.typeface = typeface
            color = textColor
            this.textSize = textSize
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        val baseline = -paint.ascent()
        val width = paint.measureText(text).toInt()
        val height = (baseline + paint.descent()).toInt()

        val drawable = BitmapDrawable(
            Resources.getSystem(),
            Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        )

        val canvas = Canvas(drawable.bitmap)
        canvas.drawText(text, (width / 2).toFloat(), baseline, paint)

        return drawable
    }

}
