package api.controller

import api.component.type.ChannelId
import api.entity.Statistics
import api.entity.Channel
import api.entity.getStatistics
import api.exeption.ApiException
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.client.WebClient
import kotlinx.coroutines.CoroutineScope
import io.github.cdimascio.dotenv.Dotenv
import io.vertx.kotlin.coroutines.coAwait


class CheckForUpdatesController (
    scope: CoroutineScope
) : RestController(scope) {
    override fun getEndpoint(): String {
        return "/check_for_updates"
    }
    // ここでエラー処理をする
    override suspend fun catchApiException(rCtx: RoutingContext, e: ApiException) {
        rCtx.response().statusCode = e.statusCode
        rCtx.response().putHeader("Content-Type", "application/json; charset=UTF-8")
        rCtx.response().write(e.renderJson().toBuffer()).coAwait()
    }

    override suspend fun handleGet(rCtx: RoutingContext) {
        val webClient = WebClient.create(rCtx.vertx())
        val dotenv = Dotenv.load()
        val apiKey = dotenv["API_KEY"]
        val endPoint = "https://www.googleapis.com/youtube/v3/channels"

        val channelId = ChannelId(rCtx.queryParams().get("channel_id").toInt())
        val part = rCtx.queryParams().get("part")
        val url = "$endPoint?part=$part&id=$channelId&key=$apiKey"

        // todo error処理する、まずresponseがnullじゃないか確認、その後に中身がちゃんと取得できているか
        val response = webClient.getAbs(url).send().coAwait()
        val body = response.bodyAsString("UTF-8")

        // videoCountが以前の数字と違う場合はtrueを返す
        val oldStatistics = Statistics(0, 0, 0)
        val newStatistics = getStatistics(response.bodyAsJsonObject().getJsonArray("items").getJsonObject(0).getJsonObject("statistics"))

        // videoCountを比較
        val videoCount = if (oldStatistics.videoCount != newStatistics.videoCount) {
            true
        } else {
            false
        }

        println(videoCount)
        rCtx.response().end(body)
    }

}