import commands.about
import commands.help
import commands.pythonTest
import commands.info.memberstats
import commands.info.serverInfo
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
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
            "info" -> {when (subCommandValue){"user" -> {memberstats(event)};"server" -> {serverInfo(event)};else -> {errorMessage("Error: This Subcommand does not seem to exist.")}}}
            "pythontest" -> {pythonTest(event)}
            else -> {errorMessage("Error: This Command does not exist in this FidelitasClient")}
        }
    }
    private fun errorMessage(message: String)
    {
        event.hook.sendMessage(message)
    }
}