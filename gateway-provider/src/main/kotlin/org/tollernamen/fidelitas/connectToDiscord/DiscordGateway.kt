package org.tollernamen.fidelitas.connectToDiscord

import okhttp3.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.tollernamen.fidelitas.discordGateway
import org.tollernamen.fidelitas.appconfig.printDiscordGatewayConfig
import org.tollernamen.fidelitas.appconfig.token
import org.tollernamen.fidelitas.websocketserver.chatHandler
import java.util.*

val gson = Gson()

var heartbeatAckReceived = true

const val os = "Linux"
const val browser = "Firefox"

var sessionId: String? = null
var connectionExists: Boolean = false
lateinit var readyPayLoad: JsonObject
var guildCreatePayLoadList: MutableList<JsonObject> = mutableListOf()

const val standardGatewayUrl = "wss://gateway.discord.gg/?v=8&encoding=json"
lateinit var resumeGatewayUrl: String

val listener = object : WebSocketListener()
{
    override fun onOpen(webSocket: WebSocket, response: Response)
    {
        printDiscordGatewayConfig("Connected to Discord Gateway")
        connectionExists = true
    }
    override fun onClosing(webSocket: WebSocket, code: Int, reason: String)
    {
        printDiscordGatewayConfig("Closing connection: $reason")
        connectionExists = false
        if (code == 4000
            || code == 4001
            || code == 4002
            || code == 4003
            || code == 4005
            || code == 4007
            || code == 4008
            || code == 4009)
        {
            reconnect()
        }
    }
    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?)
    {
        //println("Error: ${t.localizedMessage}")
        t.printStackTrace()
    }
    override fun onMessage(webSocket: WebSocket, text: String)
    {
        val jsonObject = gson.fromJson(text, JsonObject::class.java)

        when (val opcode = Opcode.fromCode(jsonObject["op"].asInt))
        {
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
            else -> printDiscordGatewayConfig("Unhandled opcode: $opcode")
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
    if (connectionExists)
    {
        discordGateway.close()
    }
    discordGateway.gatewayUrl = resumeGatewayUrl
    discordGateway.connect()
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
            resumeGatewayUrl = data["resume_gateway_url"].asString
            readyPayLoad = jsonObject
        }
        "GUILD_CREATE" -> {
            guildCreatePayLoadList.add(jsonObject)
        }
        else -> chatHandler.sendDispatch(jsonObject)
    }
}
fun handleHeartbeatAck(jsonObject: JsonObject)
{
    printDiscordGatewayConfig("Received HEARTBEAT_ACK: $jsonObject")
    heartbeatAckReceived = true
}
fun handleHello(jsonObject: JsonObject)
{
    val heartbeatInterval = jsonObject["d"].asJsonObject["heartbeat_interval"].asInt
    printDiscordGatewayConfig("Received HELLO with heartbeat interval: $heartbeatInterval")

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
    printDiscordGatewayConfig("Received HEARTBEAT with sequence number: $lastSequenceNumber")
    sendHeartbeat()
}
fun startHeartbeat(interval: Int)
{
    val heartbeatTimer = Timer()

    heartbeatTimer.scheduleAtFixedRate(object : TimerTask()
    {
        override fun run()
        {
            if (!connectionExists)
            {
                cancel()
            }
            sendHeartbeat()
            heartbeatAckReceived = false
            checkHeartBeatAckReceived(interval)
        }
    }, interval.toLong(), interval.toLong())
}
fun sendHeartbeat()
{
    val heartbeatPayload = JsonObject().apply {
        addProperty("op", Opcode.HEARTBEAT.code)
        add("d", if (lastSequenceNumber != null) gson.toJsonTree(lastSequenceNumber) else null)
    }
    discordGateway.sendMessage(heartbeatPayload.toString())
    printDiscordGatewayConfig("Sent HEARTBEAT with sequence number: $lastSequenceNumber")
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
                printDiscordGatewayConfig("Missing Heartbeat, reconnecting...")
                scheduleReconnect()
            }
        }
    }, heartbeatCheckInterval)
}
data class DiscordGateway (var gatewayUrl: String)
{
    private val client = OkHttpClient()
    private lateinit var discordWebSocket: WebSocket

    fun connect()
    {
        val request = Request.Builder().url(gatewayUrl).build()
        discordWebSocket = client.newWebSocket(request, listener)
    }
    fun close()
    {
        discordWebSocket.close(1000, "Closing connection")
        printDiscordGatewayConfig("Connection closed")
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
    discordGateway.close()

    discordGateway.connect()
}
fun scheduleReconnect()
{
    var delayMilliseconds = 2000L
    val maxDelayMilliseconds = 64000L
    val timer = Timer()
    val task = object : TimerTask()
    {
        override fun run()
        {
            startNewSession()
            printDiscordGatewayConfig("Reconnecting in ${delayMilliseconds / 1000}s")
        }
    }
    while (!connectionExists)
    {
        timer.schedule(task, delayMilliseconds)
        delayMilliseconds *= 2
        if (delayMilliseconds >= maxDelayMilliseconds)
        {
            delayMilliseconds = maxDelayMilliseconds
        }
    }
    task.cancel()
}
fun sendJsonToDiscord(jsonString: String)
{
    discordGateway.sendMessage(jsonString)
}