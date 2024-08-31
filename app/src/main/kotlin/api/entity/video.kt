package api.entity

import io.vertx.core.json.JsonObject
import java.time.LocalDateTime

data class Video (
    val id: Int,
//    val publishedAt: LocalDateTime,
    val title: String,
    val url: String,
    val description: String,
//    val thumbnail: YoutubeThumbnail,
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
//    val thumbnail = getThumbnail(json.getJsonObject("snippet").getJsonObject("thumbnails"))
    val liveBroadcastContent = LiveBroadcastContent.fromCode(json.getJsonObject("snippet").getString("liveBroadcastContent"))

    return Video(
        0,
//        publishedAt,
        title,
        url,
        description,
//        thumbnail,
        liveBroadcastContent,
    )
}

fun getThumbnail(json: JsonObject): YoutubeThumbnail {
    return YoutubeThumbnail(
        json.getString("url"),
        json.getInteger("width"),
        json.getInteger("height"),
    )
}