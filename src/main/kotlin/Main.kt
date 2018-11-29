import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.jackson.jacksonDeserializerOf
import com.github.kittinunf.result.Result
import io.mysocialapp.client.MySocialApp
import io.mysocialapp.client.Session
import io.mysocialapp.client.models.AccessControl
import io.mysocialapp.client.models.FeedPost
import io.mysocialapp.client.models.UserSettings
import kotlinx.coroutines.runBlocking

/**
 * Created by evoxmusic on 27/11/2018.
 */
fun main(vararg args: String) = runBlocking {
    if (args.size != 3) {
        throw IllegalArgumentException("3 args required: <mysocialapp_app_id> <tipstop_auth_token> <tipstop_php_session_id>")
    }

    val (appId, token, phpSessionId) = args

    (0..10).forEach { page ->
        getTipsTopData(token, phpSessionId, page).fold(
            { data -> sendDataToMSA(appId, data) },
            { error -> println(error) }
        )
    }

}

fun sendDataToMSA(appId: String, data: Data) {
    data.items?.forEach { payload ->

        val message = if (payload["comment"]?.toString()?.isBlank() == false) {
            payload["comment"].toString()
        } else {
            "ceci est un message par défaut"
        }

        val feedPost = FeedPost.Builder()
            .setMessage(message)
            .setPayload(payload)
            .setVisibility(AccessControl.PUBLIC)
            .build()


        val session = getUserSessionFromMSA(appId, payload) // get MSA user session
        session.newsFeed.blockingCreate(feedPost)

    }
}

fun getUserSessionFromMSA(appId: String, payload: Map<String, Any?>): Session {
    val userId = payload["userID"].toString()
    val email = "$userId@tipstop.co"
    val password = email.toHash()

    val msa = MySocialApp.Builder().setAppId(appId).build()

    return try {
        // get user session
        msa.blockingConnect(email, password)

    } catch (e: Exception) {

        // account does not exists in MySocialApp, create it!
        val userName = (payload["user"] as? Map<*, *>)?.get("name")?.toString() ?: email
        val session = msa.blockingCreateAccount(email, password, userName)

        val account = session.account.blockingGet().copy(
            externalId = userId, // attach Tipstop User ID to the MSA User external ID
            userSettings = UserSettings( // set user settings to French for notification messages
                languageZone = UserSettings.LanguageZone.FR,
                interfaceLanguage = UserSettings.InterfaceLanguage.FR
            )
        ).also { it.session = session } // copy does not deep copy and include session

        account.blockingSave()

        session
    }
}

suspend fun getTipsTopData(token: String, phpSessionId: String, page: Int = 1): Result<Data, FuelError> {
    val apiUrl = "https://prp.tipstop.co/data/feeds/1/1?page=$page"

    val (_, _, result) = Fuel.get(apiUrl).header(
        "Authorization" to "Basic $token",
        "Cookie" to "PHPSESSID=$phpSessionId; use_cookie=true",
        "DNT" to "1"
    ).awaitObjectResponse(jacksonDeserializerOf<Data>())

    return result
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class Data(val items: List<Map<String, Any?>>? = null)