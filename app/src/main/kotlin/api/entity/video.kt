package api.entity

import io.vertx.core.json.JsonObject
import java.time.LocalDateTime

data class Video (
    val id: Int,
//    val publishedAt: LocalDateTime,  // "publishedAt": "2024-08-29T10:00:13Z"
    val title: String,
    val url: String,
    val description: String,
//    val thumbnails: List<YoutubeThumbnail>,
    val liveBroadcastContent: LiveBroadcastContent,
)

data class YoutubeThumbnail (
    val url: String,
    val width: Int,
    val height: Int,
)

enum class LiveBroadcastContent(val value: String) {
    NONE("none"),
    UPCOMING("upcoming"),
    LIVE("live");

    companion object {
        private val map = entries.associateBy { it.value }
        fun fromCode(value: String) = map[value] ?: throw NoSuchElementException(value)
    }
}

fun getVideo(json: JsonObject): Video {
    val videoId = json.getJsonObject("id").getString("videoId")
//    val publishedAt = LocalDateTime.parse(json.getString("publishedAt"))
    val title = json.getJsonObject("snippet").getString("title")
    val url = "https://www.youtube.com/watch?v=$videoId"
    val description = json.getJsonObject("snippet").getString("description")
//    val thumbnails = getThumbnails(json.getJsonObject("snippet").getJsonObject("thumbnails"))
    val liveBroadcastContent = LiveBroadcastContent.fromCode(json.getJsonObject("snippet").getString("liveBroadcastContent"))

    return Video(
        0,
//        publishedAt,
        title,
        url,
        description,
//        thumbnails,
        liveBroadcastContent,
    )
}

// "thumbnails": {
//          "default": {
//            "url": "https://i.ytimg.com/vi/eiM6JodAJBs/default.jpg",
//            "width": 120,
//            "height": 90
//          },
//          "medium": {
//            "url": "https://i.ytimg.com/vi/eiM6JodAJBs/mqdefault.jpg",
//            "width": 320,
//            "height": 180
//          },
//          "high": {
//            "url": "https://i.ytimg.com/vi/eiM6JodAJBs/hqdefault.jpg",
//            "width": 480,
//            "height": 360
//          }
//        },
fun getThumbnails(json: JsonObject): List<YoutubeThumbnail> {
    return json.fieldNames().map { key ->
        val thumbnail = json.getJsonObject(key)
        YoutubeThumbnail(
            thumbnail.getString("url"),
            thumbnail.getInteger("width"),
            thumbnail.getInteger("height"),
        )
    }
}