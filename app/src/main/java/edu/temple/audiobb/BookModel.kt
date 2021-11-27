package edu.temple.audiobb
import com.squareup.moshi.JsonClass
import org.json.JSONObject
import java.io.Serializable
@JsonClass(generateAdapter = true)
data class BookModel(
    var id: Int,
    var title:String,
    var author:String,
    var cover_url: String,
    var duration: Int
    ) : Serializable