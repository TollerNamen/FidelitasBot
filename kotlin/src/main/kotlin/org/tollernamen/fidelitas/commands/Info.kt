package org.tollernamen.fidelitas.commands

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.tollernamen.fidelitas.CommandHandler
import org.tollernamen.fidelitas.commands.sub.info.MemberInfoHandler
import org.tollernamen.fidelitas.commands.sub.info.ServerInfoHandler
import org.tollernamen.fidelitas.SubCommandHandler

class InfoCommandHandler : CommandHandler
{
    private val subCommandHandlers: MutableMap<String, SubCommandHandler> = mutableMapOf(
        "user" to MemberInfoHandler(),
        "server" to ServerInfoHandler()
    )
    override fun handle(event: SlashCommandInteractionEvent)
    {
        try
        {
            val subCommandName: String = event.getOption("subcommand")?.asString ?: "user"
            subCommandHandlers[subCommandName]?.handle(event)
        }
        catch (e: NullPointerException)
        {
            println(e.message)
            event.hook.sendMessage("Error ${e.message}")
        }
    }
}