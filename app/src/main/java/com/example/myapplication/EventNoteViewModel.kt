package com.example.myapplication

import EventNote
import com.example.myapplication.base.BaseViewModel
import com.google.firebase.database.DatabaseError

class EventNoteViewModel : BaseViewModel() {

    fun addEvent(eventNote: EventNote,onComplete: (Boolean) -> Unit) {
        eventNoteRepository.addEvent(eventNote) { isSuccess ->
            onComplete(isSuccess)
        }
    }
    fun getAllEventNotes(
        onDataReceived: (List<EventNote>) -> Unit,
        onError: (DatabaseError) -> Unit
    ) {
        eventNoteRepository.getAllEventNotes(
            onDataReceived = { eventNotes ->
                onDataReceived(eventNotes)
            },
            onError = { error ->
                onError(error)
            }
        )
    }}