package api.controller

import io.vertx.ext.web.RoutingContext
import kotlinx.coroutines.CoroutineScope
import io.vertx.ext.web.client.WebClient

class LiveVideoSearchController (
    scope: CoroutineScope
) : RestController (scope) {
    override fun getEndpoint(): String {
        return "/liveVideoSearch"
    }

    override suspend fun handleGet(rCtx: RoutingContext) {
        val webClient = WebClient.create(rCtx.vertx())
        val endPoint = "https://www.googleapis.com/youtube/v3/search"
        val part = "snippet"
        val channelId = rCtx.queryParams().get("channel_id")
        val apiKey = ""
        val order = "date"
        val type = "video"
        val eventType = "live"
    }
}