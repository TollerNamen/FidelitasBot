package org.tollernamen.fidelitas.connectToDiscord

import okhttp3.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.tollernamen.fidelitas.discordGateway
import org.tollernamen.fidelitas.token
import org.tollernamen.fidelitas.websocketserver.chatHandler
import java.util.*
import kotlin.concurrent.schedule

val gson = Gson()

var heartbeatAckReceived = true

const val os = "Linux"
const val browser = "Firefox"

var sessionId: String? = null

var readyPayLoad: JsonObject? = null
var guildCreatePayLoadList: MutableList<JsonObject>? = mutableListOf()

val listener = object : WebSocketListener()
{
    override fun onOpen(webSocket: WebSocket, response: Response)
    {
        println("Connected to Discord Gateway")
    }
    override fun onClosing(webSocket: WebSocket, code: Int, reason: String)
    {
        println("Closing connection: $reason")
    }
    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?)
    {
        println("Error: ${t.localizedMessage}")
    }
    override fun onMessage(webSocket: WebSocket, text: String)
    {
        println("Received message: $text")
        val jsonObject = gson.fromJson(text, JsonObject::class.java)

        when (val opcode = Opcode.fromCode(jsonObject["op"].asInt))
        {
            // Handle the received Opcode
            Opcode.DISPATCH -> handleDispatch(jsonObject)
            Opcode.HEARTBEAT -> handleHeartbeat(jsonObject)
            Opcode.HELLO -> handleHello(jsonObject)
            Opcode.HEARTBEAT_ACK -> handleHeartbeatAck(jsonObject)
            Opcode.RECONNECT -> reconnect()
            Opcode.INVALID_SESSION ->
            {
                val canResume = jsonObject["d"].asBoolean
                if (canResume)
                {
                    reconnect()
                }
                else
                {
                    startNewSession()
                }
            }
            else -> println("Unhandled opcode: $opcode")
        }
    }
}
fun reconnect()
{
    if (sessionId == null || lastSequenceNumber == null)
    {
        // If sessionId or lastSequenceNumber is not available, establish a new connection
        startNewSession()
        return
    }
    val resumePayload = mapOf(
        "op" to Opcode.RESUME.code,
        "d" to mapOf(
            "token" to token,
            "session_id" to sessionId,
            "seq" to lastSequenceNumber
        )
    )
    discordGateway.sendMessage(gson.toJson(resumePayload))
}
fun handleDispatch(jsonObject: JsonObject)
{
    val eventName = jsonObject["t"].asString
    val data = jsonObject["d"].asJsonObject
    val sequenceNumber = jsonObject["s"].asInt

    lastSequenceNumber = sequenceNumber

    when (eventName)
    {
        "READY" -> {
            sessionId = data["session_id"].asString
            readyPayLoad = jsonObject
        }
        "GUILD_CREATE" -> {
            guildCreatePayLoadList?.add(jsonObject)
        }
        else -> chatHandler.sendDispatch(jsonObject)
    }
}
fun handleHeartbeatAck(jsonObject: JsonObject)
{
    println("Received HEARTBEAT_ACK: $jsonObject")
    heartbeatAckReceived = true
}
fun handleHello(jsonObject: JsonObject)
{
    val heartbeatInterval = jsonObject["d"].asJsonObject["heartbeat_interval"].asInt  //.getJSONObject("d").getInt("heartbeat_interval")
    println("Received HELLO with heartbeat interval: $heartbeatInterval")

    /*
    Intents and their corresponding values:
    GUILDS:                    1 << 0
    GUILD_MEMBERS:             1 << 1
    GUILD_BANS:                1 << 2
    GUILD_EMOJIS_AND_STICKERS: 1 << 3
    GUILD_INTEGRATIONS:        1 << 4
    GUILD_WEBHOOKS:            1 << 5
    GUILD_INVITES:             1 << 6
    GUILD_VOICE_STATES:        1 << 7
    GUILD_PRESENCES:           1 << 8
    GUILD_MESSAGES:            1 << 9
    GUILD_MESSAGE_REACTIONS:   1 << 10
    GUILD_MESSAGE_TYPING:      1 << 11
    DIRECT_MESSAGES:           1 << 12
    DIRECT_MESSAGE_REACTIONS:  1 << 13
    DIRECT_MESSAGE_TYPING:     1 << 14
     */
    val intentsValue = (1 shl 0) or
                (1 shl 1) or
                (1 shl 2) or
                (1 shl 3) or
                (1 shl 4) or
                (1 shl 5) or
                (1 shl 6) or
                (1 shl 7) or
                (1 shl 8) or
                (1 shl 9) or
                (1 shl 10) or
                (1 shl 11) or
                (1 shl 12) or
                (1 shl 13) or
                (1 shl 14)

    val identifyPayload = JsonObject().apply {
        addProperty("op", Opcode.IDENTIFY.code)
        add("d", JsonObject().apply {
            addProperty("token", token)
            add("properties", JsonObject().apply {
                addProperty("\$os", os)
                addProperty("\$browser", browser)
                addProperty("\$device", "laptop")
            })
            addProperty("compress", false)
            addProperty("large_threshold", 50)
            addProperty("intents", intentsValue)
        })
    }
    discordGateway.sendMessage(identifyPayload.toString())

    startHeartbeat(heartbeatInterval)
}
var lastSequenceNumber: Int? = null

fun handleHeartbeat(jsonObject: JsonObject)
{
    lastSequenceNumber = jsonObject["s"].asInt
    println("Received HEARTBEAT with sequence number: $lastSequenceNumber")
    sendHeartbeat()
}
fun startHeartbeat(interval: Int)
{
    val heartbeatTimer = Timer()

    heartbeatTimer.scheduleAtFixedRate(object : TimerTask()
    {
        override fun run()
        {
            sendHeartbeat()
            heartbeatAckReceived = false
            checkHeartBeatAckReceived(interval)
        }
    }, interval.toLong(), interval.toLong())
}
fun checkHeartBeatAckReceived(interval: Int)
{
    val heartbeatCheckInterval = interval.toLong() + 5000
    val heartbeatAckTimeoutTimer = Timer()
    heartbeatAckTimeoutTimer.schedule(object : TimerTask()
    {
        override fun run()
        {
            if (!heartbeatAckReceived)
            {
                println("HEARTBEAT_ACK not received, reconnecting...")
                //reconnect()
                scheduleReconnect()
            }
        }
    }, heartbeatCheckInterval)
}
fun sendHeartbeat()
{
    val heartbeatPayload = JsonObject().apply {
        addProperty("op", Opcode.HEARTBEAT.code)
        add("d", if (lastSequenceNumber != null) gson.toJsonTree(lastSequenceNumber) else null)
    }
    discordGateway.sendMessage(heartbeatPayload.toString())
    println("Sent HEARTBEAT with sequence number: $lastSequenceNumber")
}
data class DiscordGateway (var gatewayUrl: String)
{
    private val client = OkHttpClient()
    private val request = Request.Builder().url(gatewayUrl).build()
    private lateinit var discordWebSocket: WebSocket

    fun connect(listener: WebSocketListener)
    {
        discordWebSocket = client.newWebSocket(request, listener)
    }
    fun close()
    {
        discordWebSocket.close(1000, "Closing connection")
        println("Connection closed")
    }
    fun sendMessage(json: String)
    {
        discordWebSocket.send(json)
    }
}
enum class Opcode(val code: Int)
{
    DISPATCH(0),
    HEARTBEAT(1),
    IDENTIFY(2),
    STATUS_UPDATE(3),
    VOICE_STATE_UPDATE(4),
    //VOICE_SERVER_PING(5),
    RESUME(6),
    RECONNECT(7),
    REQUEST_GUILD_MEMBERS(8),
    INVALID_SESSION(9),
    HELLO(10),
    HEARTBEAT_ACK(11);

    companion object
    {
        private val opcodeMap = values().associateBy(Opcode::code)
        fun fromCode(code: Int) = opcodeMap[code]
    }
}
fun startNewSession()
{
    // Close the existing connection (if any)
    discordGateway.close()

    // Establish a new connection
    discordGateway.connect(listener)
}
fun scheduleReconnect()
{
    var delay = 2000L
    val maxDelay = 64000L
    val timer = Timer()
    while (delay <= maxDelay)
    {
        timer.schedule(delay)
        {
            startNewSession()
        }
        delay *= 2
    }
}
fun sendJsonToDiscord(jsonString: String)
{
    discordGateway.sendMessage(jsonString)
}