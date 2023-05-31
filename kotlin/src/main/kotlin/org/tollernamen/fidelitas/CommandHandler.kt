package org.tollernamen.fidelitas

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.tollernamen.fidelitas.commands.AboutCommandHandler
import org.tollernamen.fidelitas.commands.InfoCommandHandler

class SlashCommandListener : ListenerAdapter()
{
    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent)
    {
        event.deferReply(false).queue()
        val commandName: String = event.name
        val commandHandlers: MutableMap<String, CommandHandler> = mutableMapOf(
            "help" to AboutCommandHandler(),
            "about" to AboutCommandHandler(),
            "info" to InfoCommandHandler()
        )
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