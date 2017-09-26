package com.gmaslowski.telemetry.ws.api

import com.gmaslowski.telemetry.live.model.CarConfigurationInformation.{CarConfiguration, Engine, Team, Tyre}
import com.gmaslowski.telemetry.live.model.LapInformation.LapData
import com.gmaslowski.telemetry.ws.api.TelemetryWebSocketApi.{JsonApi, LiveData}
import play.api.libs.json.{Json, Writes}
import play.api.mvc.WebSocket.MessageFlowTransformer

object TelemetryWebSocketApi {

  trait JsonApi

  case class LiveData(speed: Int,
                      gear: Int,
                      sector_1: Float,
                      sector_2: Float,
                      lap: Float,
                      revs: Int,
                      throttle: Float,
                      break: Float,
                      x: Float,
                      y: Float,
                      dataType: String = "LiveData") extends JsonApi

  val EmptyLiveData = LiveData(0, 0, 0.0f, 0.0f, 0.0f, 0, 0, 0, 0, 0)
}

trait TelemetryWebSocketApiTransformers {

  implicit val jsonApiWrites: Writes[JsonApi] = {
    case s: LiveData => liveDataWrites.writes(s)
    case s: CarConfiguration => carConfigurationWrites.writes(s)
    case s: LapData => lapDataWrites.writes(s)
  }

  implicit val liveDataWrites = Json.writes[LiveData]
  implicit val lapDataWrites = Json.writes[LapData]
  implicit val tyreWrites = Json.writes[Tyre]
  implicit val engineWrites = Json.writes[Engine]
  implicit val teamWrites = Json.writes[Team]
  implicit val carConfigurationWrites = Json.writes[CarConfiguration]

  implicit val jsonApiTransformer = MessageFlowTransformer.jsonMessageFlowTransformer[String, JsonApi]
}