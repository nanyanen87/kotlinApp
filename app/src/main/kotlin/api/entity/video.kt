package api.entity

import io.vertx.core.json.JsonObject

data class Video (
    val id: Int,
    val publishedAt: String,
    val title: String,
    val url: String,
    val description: String,
    val thumbnail: YoutubeThumbnail,
    val liveBroadcastContent: LiveBroadcastContent,
)

data class YoutubeThumbnail (
    val url: String,
    val width: Int,
    val height: Int,
)
// enum class
enum class LiveBroadcastContent(val value: String) {
    NONE("none"),
    UPCOMING("upcoming"),
    LIVE("live"),
}

fun getVideo(json: JsonObject): Video {
    return Video(
        json.getString("id").toInt(),
        json.getString("publishedAt"),
        json.getString("title"),
        json.getString("url"),
        json.getString("description"),
        getThumbnail(json.getJsonObject("thumbnail")),
        LiveBroadcastContent.valueOf(json.getString("liveBroadcastContent")),
    )
}

fun getThumbnail(json: JsonObject): YoutubeThumbnail {
    return YoutubeThumbnail(
        json.getString("url"),
        json.getInteger("width"),
        json.getInteger("height"),
    )
}