import commands.AboutCommandHandler
import commands.HelpCommandHandler
import commands.InfoCommandHandler
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class AssignHandlersToInteraction(event: SlashCommandInteractionEvent)
{
    private val commandName: String = event.name
    private val commandHandlers: MutableMap<String, CommandHandler> = mutableMapOf(
        "help" to HelpCommandHandler(),
        "about" to AboutCommandHandler(),
        "info" to InfoCommandHandler()
    )
    init
    {
        commandHandlers[commandName]?.handle(event)
    }
}
interface CommandHandler
{
    fun handle(event: SlashCommandInteractionEvent)
}
interface SubCommandHandler
{
    fun handle(event: SlashCommandInteractionEvent)
}