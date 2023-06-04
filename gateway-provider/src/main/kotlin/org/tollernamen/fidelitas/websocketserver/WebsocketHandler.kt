package org.tollernamen.fidelitas.websocketserver

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.CloseStatus.GOING_AWAY
import org.springframework.web.socket.CloseStatus.POLICY_VIOLATION
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import org.tollernamen.fidelitas.connectToDiscord.Opcode
import org.tollernamen.fidelitas.connectToDiscord.guildCreatePayLoadList
import org.tollernamen.fidelitas.connectToDiscord.readyPayLoad
import org.tollernamen.fidelitas.connectToDiscord.sendJsonToDiscord
import org.tollernamen.fidelitas.appconfig.token
import org.tollernamen.fidelitas.appconfig.printWebSocketConfig
import java.util.concurrent.ConcurrentHashMap

class ChatHandler : TextWebSocketHandler()
{
    private val sessions = ConcurrentHashMap<String, WebSocketSession>()
    override fun handleTextMessage(session: WebSocketSession, message: TextMessage)
    {
        printWebSocketConfig("WebsocketServer: New TextMessage from Session: ${session.id}")
        printWebSocketConfig("WebsocketServer: Message: ${message.payload}")
        val jsonString = message.payload
        val jsonObject = JsonParser.parseString(jsonString).asJsonObject

        when (val opcode = Opcode.fromCode(jsonObject["op"].asInt))
        {
            Opcode.IDENTIFY -> handleIdentifyAndSendReady(session, jsonObject)
            Opcode.HEARTBEAT -> sendHeartBeatAck(session)
            Opcode.REQUEST_GUILD_MEMBERS -> sendJsonToDiscord(jsonString)
            Opcode.VOICE_STATE_UPDATE -> sendJsonToDiscord(jsonString)
            Opcode.STATUS_UPDATE -> sendJsonToDiscord(jsonString)
            else -> printWebSocketConfig("Unhandled opcode: $opcode")
        }
    }
    override fun afterConnectionEstablished(session: WebSocketSession)
    {
        sessions[session.id] = session
        printWebSocketConfig("WebsocketServer: New Connection Established, Session: ${session.id}")
        sendHello(session)
    }
    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus)
    {
        sessions.remove(session.id)
        printWebSocketConfig("WebsocketServer: Connection closed, Session: ${session.id}")
    }
    fun sendDispatch(jsonObject: JsonObject)
    {
        printWebSocketConfig("WebsocketServer: Sending Dispatch of type \"${jsonObject["t"].asString}\"")
        val textMessage = TextMessage(jsonObject.toString())
        for ((_, session) in sessions)
        {
            synchronized(session)
            {
                session.sendMessage(textMessage)
            }
        }
        printWebSocketConfig("WebsocketServer: Dispatch sent!")
    }
    // See Main Method for execution of this function
    fun closeOnShutDown()
    {
        for ((_, session) in sessions)
        {
            synchronized(session)
            {
                session.close(CloseStatus(GOING_AWAY.code, "Server is shutting down"))
            }
        }
    }
}
fun sendHello(session: WebSocketSession)
{
    val helloPayload = JsonObject().apply {
        addProperty("op", Opcode.HELLO.code)
        add("d", JsonObject().apply {
            addProperty("heartbeat_interval", 45000)
        })
    }
    session.sendMessage(TextMessage(helloPayload.toString()))
}
fun handleIdentifyAndSendReady(session: WebSocketSession, jsonObject: JsonObject)
{
    val d = jsonObject["d"].asJsonObject
    val serviceToken = d["token"].asString

    if (serviceToken != token)
    {
        session.close(CloseStatus(POLICY_VIOLATION.code, "Invalid Authorization"))
    }
    else
    {
        printWebSocketConfig("WebsocketServer: Sending ReadyPayload to Session: ${session.id}")
        session.sendMessage(TextMessage(readyPayLoad.toString()))
        printWebSocketConfig("WebsocketServer: Sent ReadyPayload to Session: ${session.id}")
        printWebSocketConfig("WebsocketServer: Sending GuildCreatePayloadList to Session: ${session.id}")
        for (guildCreatePayLoad in guildCreatePayLoadList)
        {
            session.sendMessage(TextMessage(guildCreatePayLoad.toString()))
        }
        printWebSocketConfig("WebsocketServer: Sent GuildCreatePayloadList to Session: ${session.id}")
    }
}
fun sendHeartBeatAck(session: WebSocketSession)
{
    val heartBeatAckPayLoad = JsonObject().apply {
        addProperty("op", Opcode.HEARTBEAT_ACK.code)
    }
    session.sendMessage(TextMessage(heartBeatAckPayLoad.toString()))
}