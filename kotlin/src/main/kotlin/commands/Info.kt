package commands

import CommandHandler
import SubCommandHandler
import commands.infoSubCommands.MemberInfoHandler
import commands.infoSubCommands.ServerInfoHandler
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class InfoCommandHandler : CommandHandler
{
    private val subCommandHandlers: MutableMap<String, SubCommandHandler> = mutableMapOf(
        "User" to MemberInfoHandler(),
        "Server" to ServerInfoHandler()
    )
    override fun handle(event: SlashCommandInteractionEvent)
    {
        val subCommandName: String? = event.getOption("subcommand")?.name
        subCommandHandlers[subCommandName]?.handle(event)
    }
}