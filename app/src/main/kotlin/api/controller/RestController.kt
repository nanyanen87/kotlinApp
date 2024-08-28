package api.controller

import api.exeption.ApiException
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.core.http.HttpMethod
import io.vertx.kotlin.coroutines.coAwait
import io.vertx.sqlclient.Pool
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


abstract class RestController(
    private val scope: CoroutineScope

) {
    //private val scope: CoroutineScope = scope
//    protected val logger: Logger = LoggerFactory.getLogger(this.javaClass.simpleName)


    protected open suspend fun handleGet(rCtx: RoutingContext) = throwUndefinedRestMethodException("GET")
    protected open suspend fun handlePut(rCtx: RoutingContext) = throwUndefinedRestMethodException("PUT")
    protected open suspend fun handlePost(rCtx: RoutingContext) = throwUndefinedRestMethodException("POST")
    protected open suspend fun handleDelete(rCtx: RoutingContext) = throwUndefinedRestMethodException("DELETE")

    private fun throwUndefinedRestMethodException(method: String) {
        // 例外を投げる
        throw RuntimeException("Method $method is not defined")
    }

    // todo catchExceptionfunを作る
//    protected open suspend fun catchApiException(rCtx: RoutingContext, e: ApiException) {
//        // デフォルトではエラーを JSON とステータスコードで返す
//        rCtx.response().statusCode = e.statusCode
//        rCtx.response().putHeader("Content-Type", "application/json; charset=UTF-8")
//        rCtx.response().write(e.renderJson().toBuffer()).coAwait()
//    }

    abstract fun getEndpoint(): String

    fun addRoute(router: Router) {
        // getEndpoint()
        // this::handleGet
        router.route(HttpMethod.GET, getEndpoint()).handler { rCtx ->
            scope.launch { handleGet(rCtx) }
        }
        router.route(HttpMethod.PUT, getEndpoint()).handler { rCtx ->
            scope.launch { handlePut(rCtx) }
        }
        router.route(HttpMethod.POST, getEndpoint()).handler { rCtx ->
            scope.launch { handlePost(rCtx) }
        }
        router.route(HttpMethod.DELETE, getEndpoint()).handler { rCtx ->
            scope.launch { handleDelete(rCtx) }
        }
    }

    protected fun runHandler(
        rCtx: RoutingContext,
        handler: suspend (RoutingContext) -> Unit
    ) {
        scope.launch {
            try {
                handler(rCtx)
            } catch (e: Throwable) {
                try {
                    when (e) {
                        // 定義済みのエラーハンドリング
                        is ApiException -> {
                            // ログ出力
//                            when (e.level) {
//                                ApiException.Level.WARN -> logger.warn("${getAccessLog(rCtx)}\t${e.serverMsg}", e)
//                                ApiException.Level.ERROR -> logger.error("${getAccessLog(rCtx)}\t${e.serverMsg}", e)
//                            }
                            catchApiException(rCtx, e)
                        }
                        // 未定義のエラーのハンドリング
                        else -> { throw e}
                    }

                } catch (e: Throwable) {
//                    logger.error("エラーハンドリング中にエラーが発生しました (${e.message})", e)
                }
            } finally {
                if (!rCtx.response().ended() && !rCtx.response().closed()) {
                    rCtx.response().end().coAwait()
                }
            }
        }

    }

    protected open suspend fun catchApiException(rCtx: RoutingContext, e: ApiException) {
        // デフォルトではエラーを JSON とステータスコードで返す
        rCtx.response().statusCode = e.statusCode
        rCtx.response().putHeader("Content-Type", "application/json; charset=UTF-8")
        rCtx.response().write(e.renderJson().toBuffer()).coAwait()
    }

}