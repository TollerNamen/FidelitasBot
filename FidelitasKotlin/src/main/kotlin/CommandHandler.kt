import commands.about
import commands.help
import commands.pythonTest
import commands.info.memberstats
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.OptionData

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
    Commands.slash("about", "show what the application is all about"),
    Commands.slash("pythontest", "test python implementation")
)
class CommandHandler(private val event: SlashCommandInteractionEvent) : ListenerAdapter()
{
    private val commandName: String = event.name
    private val subCommandValue: String = if (event.options.size > 0)
    {event.options[0].asString} else {"defaultSubCommand"}

    fun commandHandler()
    {
        when (commandName)
        {
            "about" -> {about(event)}
            "help" -> {help(event)}
            "info" -> {infoSubCommand()}
            "pythontest" -> {pythonTest(event)}
            else -> {errorMessage("Error: This Command does not exist in this FidelitasClient")}
        }
    }
    private fun infoSubCommand()
    {
        when (subCommandValue)
        {
            "user" -> {memberstats(event)}
            "server" -> {}
            else -> {errorMessage("Error: This Subcommand does not seem to exist.")}
        }
    }
    private fun errorMessage(message: String) {
        event.hook.sendMessage(message)
    }
}