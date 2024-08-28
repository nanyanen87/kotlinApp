package api.entity

import api.component.type.ChannelId
import io.vertx.core.json.JsonObject

data class Channel(
    val id: ChannelId,
    val statistics: Statistics,
)

data class Statistics(
    val viewCount: Int,
    val subscriberCount: Int,
    val videoCount: Int,
)
// jsonからstatisticsを取得するfun
fun getStatistics(json: JsonObject): Statistics {
    return Statistics(
        json.getString("viewCount").toInt(),
        json.getString("subscriberCount").toInt(),
        json.getString("videoCount").toInt(),
    )
}
