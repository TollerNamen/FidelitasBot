import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import commands.stats.memberstats

val cmds: List<CommandData> = listOf(
    Commands.slash("commands/stats", "show statistics")
        .addOptions(
            OptionData(
                OptionType.STRING, "subcommand",
                "Pick an object to see its corresponding statistics",
                true, true)
                .addChoice("Server", "server")
                .addChoice("User", "user"))
)
class EventListener : ListenerAdapter()
{
    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent)
    {
        event.deferReply(true).queue()
        CommandHandler(event).commandHandler()
    }
}
class CommandHandler(private val event: SlashCommandInteractionEvent) : ListenerAdapter()
{
    private val commandName: String = event.name
    private val subCommandName: String = event.options[0].name
    fun commandHandler()
    {
        when (commandName)
        {
            "help" -> {}
            "statistics" -> {statisticSubCommand()}
            else -> {errorMessage("Error: This Command does not exist in this FidelitasClient")}
        }
    }
    private fun statisticSubCommand()
    {
        when (subCommandName)
        {
            "user" -> {
                memberstats()
            }
            "server" -> {}
            else -> {errorMessage("Error: This Subcommand does not seem to exist.")}
        }
    }
    private fun errorMessage(message: String) {
        event.hook.sendMessage(message)
    }
}