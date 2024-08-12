import api.controller.RestController
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.coroutines.coAwait
import io.vertx.mysqlclient.MySQLBuilder
import io.vertx.mysqlclient.MySQLConnectOptions
import io.vertx.sqlclient.Pool
import io.vertx.sqlclient.PoolOptions
import io.vertx.sqlclient.SqlClient
import kotlinx.coroutines.CoroutineScope


class TestController(
    private val pool: Pool,
    scope: CoroutineScope,
) : RestController(scope) {
    override fun getEndpoint(): String {
        return "/test"
    }
    // get
    override suspend fun handleGet(rCtx: RoutingContext) {
//        val webClient = WebClient.create(rCtx.vertx())
//        val url = "https://weather.tsukumijima.net/api/forecast/city/400040"
//        val response = webClient.getAbs(url).send().coAwait()
//        rCtx.response().end(response.bodyAsString())

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



        val client: SqlClient = pool
        val rows = client.query("SELECT id FROM trx_users").execute().coAwait()
        val ids = rows.map { row ->
            row.getInteger("id")
        }
        rCtx.response().end(ids.toString())
    }
}