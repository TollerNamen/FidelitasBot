import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class EventListener : ListenerAdapter()
{
    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent)
    {
        event.deferReply(false).queue()
        CommandHandler(event).commandHandler()
    }
    override fun onReady(event: ReadyEvent)
    {
        if (addCommandsToGuild)
        {
            println("Adding Commands to guilds is turned on:")
            for (guildId in guildIds)
            {
                addSlashCmds(guildId)
            }
        }
        else
        {
            println("Adding Commands to guilds is turned off.")
        }
    }
}