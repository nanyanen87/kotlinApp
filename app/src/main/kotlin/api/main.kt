/*
 * This source file was generated by the Gradle 'init' task
 */
package api

import TestController
import api.controller.CheckForUpdatesController
import api.controller.SearchLiveVideoController
import io.vertx.core.Vertx
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.coAwait
import io.vertx.mysqlclient.MySQLBuilder
import io.vertx.mysqlclient.MySQLConnectOptions
import io.vertx.sqlclient.Pool
import io.vertx.sqlclient.PoolOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    val vertx = Vertx.vertx()
    runBlocking {
        // ApiVerticle のデプロイ
        vertx.deployVerticle(
            ApiVerticle(), // この Verticle は複数のインスタンスをデプロイするので、クラスではなくインスタンス化する関数を指定する
//            DeploymentOptions()
//                .setInstances(minOf(32, VertxOptions.DEFAULT_EVENT_LOOP_POOL_SIZE))  // インスタンス数 (実質スレッド数)
//                .setWorkerPoolName(ApiVerticle::class.java.simpleName)
        ).coAwait()
        println("ready!!")
    }
}

class ApiVerticle : CoroutineVerticle() {
    private lateinit var pool: Pool
    private fun setup(router: Router) {
        val controllers = listOf(
            TestController(pool, this),
            CheckForUpdatesController(this),
            SearchLiveVideoController(this)
        )
        controllers.forEach { it.addRoute(router) }
    }
    override suspend fun start() {
        super.start()
        val conn = MySQLConnectOptions()
            .setPort(56306)
            .setHost("localhost")
            .setDatabase("xfolio")
            .setUser("linku")
            .setPassword("linku")

        this.pool = MySQLBuilder
            .pool()
            .apply {
                val poolOptions = PoolOptions()
                    .setMaxSize(5)
                this.with(poolOptions)
                this.connectingTo(conn)
                this.using(vertx)
            }
            .build()

        val router = Router.router(vertx)
        this.setup(router)


        vertx.createHttpServer()
            .requestHandler(router)
            .listen(8080).coAwait()
    }
    override suspend fun stop() {
        super.stop()
    }
}

// 一番原始的な形
//fun setup(router: Router, scope: CoroutineScope) {
////    router.route(HttpMethod.GET, "/hello").handler { rCtx ->
////      scope.launch { rCtx.response().end("Hello, World!").coAwait() }
////    }
//
//val controllers = listOf(
//    TestController()
//)
//    controllers.forEach { it.addRoute(router, scope) }
//}

//fun addRoute(
//    router: Router,
//    scope: CoroutineScope,
//    method: HttpMethod,
//    path: String,
//    handler: suspend (rCtx: RoutingContext) -> Unit
//) {
//    router.route(method, path).handler { rCtx ->
//        scope.launch { handler(rCtx) }
//    }
//}

