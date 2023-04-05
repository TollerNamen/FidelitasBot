import dev.minn.jda.ktx.interactions.commands.updateCommands
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.MemberCachePolicy
import java.util.EnumSet

private var jda: JDA? = null
private const val TOKEN = "TOKEN"

fun main(/*args: Array<String>*/) {
    println("Hello World!")

    jda = JDABuilder.createDefault(TOKEN)
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

    addSlashCmds("832328391347666964")
}

fun addSlashCmds(id: String) {
    val guild: Guild? = jda?.getGuildById(id)
    guild?.updateCommands {
        addCommands(cmds)
    }?.queue()
}
