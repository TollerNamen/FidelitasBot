package org.tollernamen.fidelitas.commands

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.tollernamen.fidelitas.CommandHandler
import org.tollernamen.fidelitas.commandData
import org.tollernamen.fidelitas.defaultEmbedColor

class HelpCommandHandler : CommandHandler
{
    override fun handle(event: SlashCommandInteractionEvent)
    {
        description(event)
        { result ->
            val embedBuilder = EmbedBuilder()
                .setTitle("Help")
                .setColor(defaultEmbedColor)
                .setDescription(result)

            println(result)

            event.hook.sendMessageEmbeds(embedBuilder.build()).queue()
        }
    }
    fun description(event: SlashCommandInteractionEvent, callback: (String) -> Unit)
    {
        event.guild?.retrieveCommands()?.queue({ commands ->
            if (commands.isEmpty())
            {
                callback("There are no slash commands available in this guild. To add commands, send a message mentioning ${event.jda.selfUser.asTag} and saying \"setup\" (not case sensitive)")
            }
            else
            {
                val description = StringBuilder()
                val allowedCommands = commands
                    .filter{ command ->
                    commandData
                        .any{
                        command.name == it.name
                    }
                }
                for (command in allowedCommands)
                {
                    description.append("${command.asMention}\n ${command.description} \n")
                }
                callback(description.toString())
            }
        }, {
            callback("Failed to retrieve commands.")
        })
    }

}