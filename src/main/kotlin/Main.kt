import dev.minn.jda.ktx.interactions.commands.updateCommands
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.MemberCachePolicy

var jda: JDA? = null

fun main(/*args: Array<String>*/) {
    println("Hello World!")

    val token = tokenFile.readText().trim()

    jda = JDABuilder.createDefault(token)
        .setActivity(Activity.listening("interactions"))
        .setStatus(OnlineStatus.ONLINE)
        .setAutoReconnect(true)
        .setChunkingFilter(ChunkingFilter.ALL)
        .setMemberCachePolicy(MemberCachePolicy.ALL)
        .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.DIRECT_MESSAGE_TYPING,
            GatewayIntent.DIRECT_MESSAGES,GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_MESSAGE_TYPING,
            GatewayIntent.GUILD_PRESENCES, GatewayIntent.MESSAGE_CONTENT)
        .addEventListeners(EventListener())
        .build()
}