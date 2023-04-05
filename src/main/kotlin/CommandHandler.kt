import commands.help
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import commands.stats.memberstats

val cmds: List<CommandData> = listOf(
    Commands.slash("statistics", "show statistics")
        .addOptions(
            OptionData(
                OptionType.STRING, "subcommand",
                "Pick an object to see its corresponding statistics",
                true, false)
                .addChoice("Server", "server")
                .addChoice("User", "user")),
    Commands.slash("help", "show what the application is all about")
)
class EventListener : ListenerAdapter()
{
    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent)
    {
        event.deferReply(false).queue()
        CommandHandler(event).commandHandler()
    }
}
class CommandHandler(private val event: SlashCommandInteractionEvent) : ListenerAdapter()
{
    private val commandName: String = event.name
    private val subCommandName: String = if (event.options.size > 0) {
        event.options[0].name
    } else {
        // Default subcommand name if no options are present
        "defaultSubCommand"
    }
    fun commandHandler()
    {
        when (commandName)
        {
            "help" -> {help(event)}
            "statistics" -> {statisticSubCommand()}
            else -> {errorMessage("Error: This Command does not exist in this FidelitasClient")}
        }
    }
    private fun statisticSubCommand()
    {
        when (subCommandName)
        {
            "user" -> {memberstats()}
            "server" -> {}
            else -> {errorMessage("Error: This Subcommand does not seem to exist.")}
        }
    }
    private fun errorMessage(message: String) {
        event.hook.sendMessage(message)
    }
}