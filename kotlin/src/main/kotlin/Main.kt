import dev.minn.jda.ktx.interactions.commands.updateCommands
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.MemberCachePolicy
import net.dv8tion.jda.api.utils.SessionControllerAdapter
import net.dv8tion.jda.internal.JDAImpl.LOG

var jda: JDA? = null

fun main(/*args: Array<String>*/)
{
    println("Hello World!")

    val token = tokenFile.readText().trim()

    var sessionController = CustomSessionController()

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
        .addEventListeners(EventListener())
        .build()

    println(sessionController.gateway)
}
class CustomSessionController : SessionControllerAdapter()
{
    final val customGatewayUrl = "ws://localhost:3000/fidelitas/gate"
    override fun getGateway(): String
    {
        return customGatewayUrl
    }
}