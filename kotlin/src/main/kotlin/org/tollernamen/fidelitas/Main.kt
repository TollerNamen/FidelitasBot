package org.tollernamen.fidelitas

import org.tollernamen.fidelitas.listeners.ButtonInteraction
import org.tollernamen.fidelitas.listeners.MessageReceived
import org.tollernamen.fidelitas.listeners.Ready
import org.tollernamen.fidelitas.listeners.StringSelectMenuInteraction
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.MemberCachePolicy
import net.dv8tion.jda.api.utils.SessionControllerAdapter

var jda: JDA? = null

fun main(/*args: Array<String>*/)
{
    println("Hello World!")

    val token = tokenFile.readText().trim()

    val sessionController = CustomSessionController()

    jda = JDABuilder.createDefault(token)
        .setActivity(Activity.listening("interactions"))
        .setStatus(OnlineStatus.ONLINE)
        .setAutoReconnect(true)
        .setChunkingFilter(ChunkingFilter.ALL)
        .setMemberCachePolicy(MemberCachePolicy.ALL)
        .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.DIRECT_MESSAGE_TYPING,
            GatewayIntent.DIRECT_MESSAGES,GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_MESSAGE_TYPING,
            GatewayIntent.GUILD_PRESENCES, GatewayIntent.MESSAGE_CONTENT)
        .setSessionController(sessionController)
        .addEventListeners(SlashCommandListener(), ButtonInteraction(), MessageReceived(), StringSelectMenuInteraction(), Ready())
        .build()

    println(sessionController.gateway)
}
class CustomSessionController : SessionControllerAdapter()
{
    private val customGatewayUrl = "ws://localhost:3000/fidelitas/gate"
    override fun getGateway(): String
    {
        return customGatewayUrl
    }
}