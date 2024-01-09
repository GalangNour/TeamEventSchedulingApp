import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class EventNote(
    val nama: String = "",
    val deskripsi: String = "",
    val team: String = "",
    val date_start: Date,
    val date_end: Date
) : Parcelable {
    // Add a no-argument constructor for Firebase deserialization
    constructor() : this("", "", "", Date(), Date())
}
