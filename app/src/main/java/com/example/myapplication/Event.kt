package com.example.tokoonline.data.model.firebase

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable
import java.util.Date


@Parcelize
data class Event (
    var id: String? = null,
    val nama: String = "",
    val deskripsi: String = "",
    val team : List<String>?,
    val date_start: Date,
    val date_end : Date,
) : Parcelable, Serializable {
    fun toMap(): Map<String, Any?> {
        val gson = Gson()
        val json = gson.toJson(this)
        return gson.fromJson(json, object : TypeToken<Map<String, Any>>() {}.type)
    }
}