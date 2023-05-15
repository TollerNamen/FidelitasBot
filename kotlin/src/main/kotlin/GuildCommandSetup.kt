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

    /*
    val commandId = "1095810027048140831"

    guild.deleteCommandById(commandId).queue(
        { println("Command deleted successfully.") },
        { error -> println("Failed to delete command: ${error.message}") }
    )
     */

    guild.updateCommands {
        addCommands(commandData)
    }
        .queue {
        when (sendMessage)
        {
            true -> event?.hook?.sendMessage("Commands updated successfully!")?.queue()
            else -> println("Commands updated successfully")
        }
    }
}
val commandData: List<CommandData> = listOf(
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
    Commands.slash("help", "show all available commands")
)