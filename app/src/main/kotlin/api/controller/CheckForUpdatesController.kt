package api.controller

import api.controller.RestController
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.client.WebClient
import kotlinx.coroutines.CoroutineScope

class CheckForUpdatesController (
    scope: CoroutineScope
) : RestController(scope) {
    override fun getEndpoint(): String {
        return "/checkForUpdates"
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