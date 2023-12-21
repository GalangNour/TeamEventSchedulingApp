package com.example.myapplication

import EventNote
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.Flow

class EventNoteRepository {

    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("EventNote")

    companion object {
        @Volatile
        private var INSTANCE: EventNoteRepository? = null

        fun getInstance(): EventNoteRepository {
            return INSTANCE ?: synchronized(this) {

                val instance = EventNoteRepository()
                INSTANCE = instance
                instance
            }
        }
    }

    fun addEvent(eventnote: EventNote, onComplete: (isSuccess: Boolean) -> Unit) {
        // Get a reference to the "EventNote" table in the Firebase Realtime Database
        val eventNoteRef = databaseReference.push() // Generate a unique ID

        // Set the value of the event under the unique ID
        eventNoteRef.setValue(eventnote)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }

    fun getAllEventNotes(
        onDataReceived: (List<EventNote>) -> Unit,
        onError: (DatabaseError) -> Unit
    ) {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val eventNotes = mutableListOf<EventNote>()

                // Iterate through the children (unique keys) of the snapshot
                for (childSnapshot in snapshot.children) {
                    // Deserialize the childSnapshot into an EventNote object
                    val eventNote = childSnapshot.getValue(EventNote::class.java)
                    eventNote?.let {
                        eventNotes.add(it)
                    }
                }

                // Pass the list of EventNotes to the callback
                onDataReceived(eventNotes)
            }

            override fun onCancelled(error: DatabaseError) {
                // Pass the error to the onError callback
                onError(error)
            }
        })
    }
}