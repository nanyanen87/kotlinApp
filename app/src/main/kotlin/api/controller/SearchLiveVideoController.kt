package api.controller

import api.entity.LiveBroadcastContent
import api.entity.getVideo
import io.github.cdimascio.dotenv.Dotenv
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import kotlinx.coroutines.CoroutineScope
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.coroutines.coAwait

/**
 * ライブ配信中のvideoIdを取得する
 */
class SearchLiveVideoController (
    scope: CoroutineScope
) : RestController (scope) {
    override fun getEndpoint(): String {
        return "/search_live_video"
    }

    override suspend fun handleGet(rCtx: RoutingContext) {
        val webClient = WebClient.create(rCtx.vertx())
        val dotenv = Dotenv.load()
        val apiKey = dotenv["API_KEY"]
        val channelId = rCtx.queryParams().get("channel_id")


        val endPoint = "https://www.googleapis.com/youtube/v3/search"

        // params
//        val channelId = "UCV2vH-9d4UHEbnRjKC0BBmw" // arise
//        val channelId = "UCu2Fxqf37DAZ0ZhIsd1FZwA" // st6
//        val channelId = "UC5pQNAOnkkly0doFFDsBPxw" // umehara
        val part = "snippet"
        val order = "date"
        val type = "video"
//        val eventType = "live" channelIdと併用できない？itemsが空になる
        val maxResults = 10 // 5-50

        val url = "$endPoint?part=$part&channelId=$channelId&key=$apiKey&order=$order&type=$type&maxResults=$maxResults"
        val response = webClient.getAbs(url).send().coAwait()

        // test
        val bodyString = response.bodyAsString("UTF-8")
        println(bodyString)

        val videos = response.bodyAsJsonObject().getJsonArray("items").map { item ->
            item as JsonObject
            getVideo(item)
        }


        // live配信のvideoIdを一つ取得
        val videoOnLive = videos.firstOrNull { it.liveBroadcastContent == LiveBroadcastContent.LIVE }
        // live配信のvideoIdを取得

        // videoIdがない場合はエラーを返す
        if (videoOnLive == null) {
            rCtx.response().setStatusCode(404).end("live video not found")
            return
        }
        rCtx.response().end(videoOnLive.url)

    }
}