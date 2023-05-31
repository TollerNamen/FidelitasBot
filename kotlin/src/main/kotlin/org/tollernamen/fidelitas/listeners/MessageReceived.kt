package org.tollernamen.fidelitas.listeners

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.buttons.Button

class MessageReceived : ListenerAdapter()
{
    override fun onMessageReceived(event: MessageReceivedEvent)
    {
        val regex = Regex("setup", RegexOption.IGNORE_CASE)
        val mentionedUsers = event.message.mentions.users
        if (regex.containsMatchIn(event.message.contentRaw) && mentionedUsers.contains(event.jda.selfUser))
        {
            println("message acknowledged")
            event.channel.sendMessage("Add all SlashCommands to your Server!").addActionRow(Button.success("addSlashCmds", "Add SlashCommands to Server")).queue()
        }
    }
}