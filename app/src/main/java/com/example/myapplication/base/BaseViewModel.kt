package com.example.myapplication.base

import androidx.lifecycle.ViewModel
import com.example.myapplication.EventNoteRepository

abstract class BaseViewModel: ViewModel() {
        val eventNoteRepository : EventNoteRepository = EventNoteRepository.getInstance()
}