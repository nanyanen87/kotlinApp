package api.exeption

import io.vertx.core.json.JsonObject

sealed class ApiException(
    override val cause: Throwable? = null,
) : RuntimeException(cause) {
    /**
     * HTTP のステータスコード
     */
    abstract val statusCode: Int

    /** サーバー側でログを残すときのログレベル
     * - zabbix → slack に通知するものかどうかの判断に使われる想定
     */
    abstract val level: Level

    /** フロント(アプリ・web) に渡すエラーアラート */
    abstract val frontAlert: FrontAlert

    /** サーバーサイド (つまりログ用) のエラーメッセージ */
    abstract val serverMsg: String?

    abstract val type: Type

    /** ログレベル */
    enum class Level {
        WARN,
        ERROR
    }

    enum class Type {
        DEFAULT,
        MAINTENANCE,
        USER_REGISTER,
        LOGIN_REQUIRED,
        PROFILE_REQUIRED,
        FORCED_UPDATE
    }

    /**
     * フロントに返すアラート
     */
    data class FrontAlert(
        /** 件名 */
        val subject: String,

        /** 本文 */
        val body: String,
    )

    /**
     * 異常系レスポンスに埋め込むメッセージ
     *   - あくまでメッセージを返すだけに留める
     *   - 過度な高機能化はしないようにすること！！！
     *   - 例えばリダイレクトなどは異常系レスポンスでハンドリングしない
     *     - 画面遷移は基本的にアプリや web フロントに任せる
     *     - もしくは HTTP の基本機能としての 301 ステータスコードを使う
     */
    fun renderJson(): JsonObject {
        val json = JsonObject()
        json.put("subject", frontAlert.subject)
        json.put("body", frontAlert.body)
        return json
    }
}


/**
 * 全ての画面で共通のエラーとしての例外
 *   - どの画面にも共通するようなエラーの親クラスとして使う
 *   - 明らかに特定の画面に紐づくエラーはこちらを使わない
 */
sealed class CommonException(
    override val cause: Throwable? = null,
) : ApiException(cause = cause)

/**
 * 画面ごとに用意するエラーとしての例外
 *   - 画面に紐づくエラーの親クラスとして使う
 */
abstract class ApplicationException(
    override val cause: Throwable? = null,
) : ApiException(cause = cause)