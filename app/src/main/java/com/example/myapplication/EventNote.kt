import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable
import java.util.Date

@Parcelize
data class EventNote (
    val id: String? = null, // Keep this as a nullable property
    val nama: String = "",
    val deskripsi: String = "",
    val team: List<String>? = null, // You can keep it nullable
    val date_start: Date,
    val date_end: Date,
) : Parcelable, Serializable {
    fun toMap(): Map<String, Any?> {
        val gson = Gson()
        val json = gson.toJson(this)
        return gson.fromJson(json, object : TypeToken<Map<String, Any>>() {}.type)
    }
}
