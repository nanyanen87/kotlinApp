import api.controller.RestController
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.coroutines.coAwait
import io.vertx.mysqlclient.MySQLBuilder
import io.vertx.mysqlclient.MySQLConnectOptions
import io.vertx.sqlclient.Pool
import io.vertx.sqlclient.PoolOptions
import io.vertx.sqlclient.SqlClient
import kotlinx.coroutines.CoroutineScope
import io.vertx.ext.web.client.WebClient


class TestController(
    private val pool: Pool,
    scope: CoroutineScope,
) : RestController(scope) { // constructorのシンタックスシュガーみたいなもの
    override fun getEndpoint(): String {
        return "/test"
    }
    // get
    override suspend fun handleGet(rCtx: RoutingContext) {
        val webClient = WebClient.create(rCtx.vertx())
        // queryパラメータを受け取る
        val channelId = rCtx.queryParams().get("channel_id")
//        val url = "https://weather.tsukumijima.net/api/forecast/city/400040"
//        val endPoint = "https://www.googleapis.com/youtube/v3/channels?"
        val endPoint = "https://www.googleapis.com/youtube/v3/search"
        val part = "snippet"
//        val channelId = "UCV2vH-9d4UHEbnRjKC0BBmw" // arise
//        val channelId = "UCfiwzLy-8yKzIbsmZTzxDgw" // arabic
        val order = "date"
        val type = "video"
        val eventType = "live"
        val apikey = ""
        val url = "$endPoint?part=$part&channelId=$channelId&key=$apiKey&order=$order&type=$type&eventType=$eventType"
        println(url)
        val response = webClient.getAbs(url).send().coAwait()
        // itemsの中のvideoIdでlive配信取得可能
        // 文字化けするので、文字コードを指定して文字列に変換
        val body = response.bodyAsString("UTF-8")

        println(body)
        rCtx.response().end(body)
//        val conn = MySQLConnectOptions()
//            .setPort(56306)
//            .setHost("localhost")
//            .setDatabase("xfolio")
//            .setUser("linku")
//            .setPassword("linku");
//
//        val pool: Pool = MySQLBuilder
//            .pool()
//            .apply {
//                val poolOptions = PoolOptions()
//                    .setMaxSize(5)
//                this.with(poolOptions)
//                this.connectingTo(conn)
//                this.using(rCtx.vertx())
//            }
//            .build()
//        val pool2: Pool  = MySQLBuilder
//            .pool()
//            .with(
//                PoolOptions()
//                    .setMaxSize(5)
//            )
//            .connectingTo(conn)
//            .using(rCtx.vertx())
//            .build()



//        val client: SqlClient = pool
//        val rows = client.query("SELECT id FROM trx_users").execute().coAwait()
//        val ids = rows.map { row ->
//            row.getInteger("id")
//        }
//        rCtx.response().end(ids.toString())
    }
}