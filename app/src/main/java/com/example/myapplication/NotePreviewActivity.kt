package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.applandeo.calendarsampleapp.MainActivity.Companion.CALENDAR_EXTRA
import com.applandeo.calendarsampleapp.MainActivity.Companion.NOTE_EXTRA
import com.applandeo.calendarsampleapp.databinding.ActivityNotePreviewBinding
//import com.applandeo.calendarsampleapp.extensions.toSimpleDate
import java.util.*

class NotePreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotePreviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotePreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val calendar = intent.getSerializableExtra(CALENDAR_EXTRA) as Calendar
//        binding.toolbar.subtitle = calendar.time.toSimpleDate()

        val note = intent.getStringExtra(NOTE_EXTRA)

        if (note != null) {
            binding.noteTextView.text = note
            binding.emptyStateTextView.isVisible = false
        }
    }
}