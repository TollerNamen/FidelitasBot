import dev.minn.jda.ktx.interactions.commands.updateCommands
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.OptionData

fun addSlashCmds(id: String, sendMessage: Boolean, event: ButtonInteractionEvent?)
{
    val guild: Guild? = jda?.getGuildById(id)
    if (guild == null)
    {
        println("Guild not found")
        return
    }
    println("Guild found: ${guild.name}")

    guild.updateCommands {
        addCommands(cmds)
    }.queue {
        when (sendMessage)
        {
            true -> {
                event?.hook?.sendMessage("Commands updated successfully!")?.queue()
            }
            else -> {println("Commands updated successfully")}
        }
    }
}
val cmds: List<CommandData> = listOf(
    Commands.slash("info", "show statistics")
        .addOptions(
            OptionData(
                OptionType.STRING, "subcommand",
                "Pick an object to see its corresponding statistics",
                true, false)
                .addChoice("Server", "server")
                .addChoice("User", "user"),
            OptionData(
                OptionType.USER, "user", "pick a another user instead of yourself"
            )
        ),
    Commands.slash("help", "show all available commands"),
    Commands.slash("about", "show what the application is all about")
)
