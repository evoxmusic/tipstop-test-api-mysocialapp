import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.mysocialapp.client.MySocialApp
import io.mysocialapp.client.models.AccessControl
import io.mysocialapp.client.models.FeedPost
import java.util.*

/**
 * Created by evoxmusic on 27/11/2018.
 */
fun main() {
    val dataFromTipsTop: Map<String, Any?> = ObjectMapper().readValue(data,
        object : TypeReference<HashMap<String, Any?>>() {})

    val session = MySocialApp.Builder()
        .setAppId("u470584465854a728453")
        .build()
        .blockingConnect("alice.jeith@mysocialapp.io", "myverysecretpassw0rd")

    val feedPost = FeedPost.Builder()
        .setMessage("This is a magic message")
        .setPayload(dataFromTipsTop)
        .setVisibility(AccessControl.PUBLIC)
        .build()

    val feed = session.newsFeed.blockingCreate(feedPost)

    assert(feed?.payload?.get("pronostics") != null)
}

val data = """
{
      "userID": "370",
      "id": "52480",
      "uniqueTip": "52471",
      "parentID": "52471",
      "datetime": "2018-11-27 16:50:27",
      "sport": "football",
      "competition": "League Cup",
      "gameID": "2888323",
      "homeTeamID": "6394",
      "homeTeam": "Nimes",
      "awayTeamID": "9853",
      "awayTeam": "Saint-Etienne",
      "gameTime": "19:00",
      "comment": "",
      "PriorityComment": "0",
      "result": "Nimes-Saint-Etienne<br>Vainqueur (Temps r\u00e9glementaire) : Saint-Etienne",
      "cote": "2.53",
      "tipTotalBet": "289.35",
      "mise": "4.50",
      "type": "bet",
      "status": "En cours",
      "totalResult": "en cours",
      "outcomeID": "483607721",
      "outcome": "Vainqueur (Temps r\u00e9glementaire) : Saint-Etienne",
      "tipLikes": "0",
      "tipComments": "0",
      "isLiked": false,
      "totalCombine": "4",
      "startdate": "2018-11-27 19:00:00",
      "followed": "1",
      "homeCountryID": "5",
      "awayCountryID": "5",
      "LEASTVALUE": "215",
      "likes": 0,
      "comments": 0,
      "ismulti": true,
      "match": {
        "title": "",
        "gameID": 2888323,
        "extra_title": "",
        "meteo": "",
        "status": "",
        "time": "19:00",
        "has_summary": false,
        "teamA": {
          "name": "Nimes",
          "shortname": ""
        },
        "teamB": {
          "name": "Saint-Etienne",
          "shortname": ""
        }
      },
      "pronostics": [
        {
          "uid": null,
          "subtitle": "FC Boca Juniors vs Gibraltar United FC",
          "outcome": "Vainqueur (Temps r\u00e9glementaire) : FC Boca Juniors",
          "bet": "Cote 8.36",
          "time": "19:00",
          "comment": "",
          "teamA": {
            "logo": "\/images\/logos\/default.png",
            "name": "FC Boca Juniors",
            "shortname": ""
          },
          "teamB": {
            "name": "Gibraltar United FC",
            "shortname": ""
          }
        },
        {
          "uid": null,
          "subtitle": "Nimes vs Saint-Etienne",
          "outcome": "Vainqueur (Temps r\u00e9glementaire) : Saint-Etienne",
          "bet": "Cote 2.53",
          "time": "19:00",
          "comment": "",
          "teamA": {
            "name": "Nimes",
            "shortname": ""
          },
          "teamB": {
            "name": "Saint-Etienne",
            "shortname": ""
          }
        },
        {
          "uid": null,
          "subtitle": "Ramsbottom United vs Pickering Town",
          "outcome": "Vainqueur (Temps r\u00e9glementaire) : Pickering Town",
          "bet": "Cote 4.50",
          "time": "19:00",
          "comment": "",
          "teamA": {
            "name": "Ramsbottom United",
            "shortname": ""
          },
          "teamB": {
            "name": "Pickering Town",
            "shortname": ""
          }
        },
        {
          "uid": null,
          "subtitle": "Ipswich Town vs Bristol City",
          "outcome": "Vainqueur (Temps r\u00e9glementaire) : Ipswich Town",
          "bet": "Cote 3.04",
          "time": "19:00",
          "comment": "",
          "teamA": {
            "name": "Ipswich Town",
            "shortname": ""
          },
          "teamB": {
            "name": "Bristol City",
            "shortname": ""
          }
        }
      ],
      "pronostic_summary": {
        "uid": "52480",
        "title": "4 matchs",
        "bet": "289.35 - 4.50"
      }
    }
""".trimIndent()