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
    val statistics = json.getJsonObject("statistics")
    return Statistics(
        statistics.getInteger("viewCount"),
        statistics.getInteger("subscriberCount"),
        statistics.getInteger("videoCount"),
    )
}
