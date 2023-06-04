package org.tollernamen.fidelitas.listeners

import org.tollernamen.fidelitas.commandData
import org.tollernamen.fidelitas.defaultEmbedColor
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import org.tollernamen.fidelitas.aboutImageUrl

class StringSelectMenuInteraction : ListenerAdapter()
{
    override fun onStringSelectInteraction(event: StringSelectInteractionEvent)
    {
        if (event.selectMenu.id == "commandBrowser")
        {
            event.deferEdit().queue()

            val newEmbed = EmbedBuilder().setColor(defaultEmbedColor)

            when (event.values[0])
            {
                "help_general" -> {
                    description(commandData, event) {
                        newEmbed.setTitle("Help - General Commands")
                            .setDescription(it)
                            .setImage(aboutImageUrl)

                        event.message.editMessageEmbeds(newEmbed.build()).queue()
                    }
                }
                "help_about" -> {
                    val name = event.jda.selfUser.name
                    newEmbed.setTitle("Help - About")
                        .setDescription(
                            """
                        $name originates from the open source FidelitasBot project on GitHub.

                        • [GitHub](https://github.com/TollerNamen/FidelitasBot)
                        • [Community](https://discord.gg/EcbnGTSMZZ)
                        • [Invite](https://discord.com/api/oauth2/authorize?client_id=1000390823273304066&permissions=8&scope=bot)

                        [Report an issue to the project](https://github.com/TollerNamen/FidelitasBot/issues)
                        """.trimIndent()
                        )
                        .setImage(aboutImageUrl)

                    event.message.editMessageEmbeds(newEmbed.build()).queue()
                }
                else -> event.reply("How did you get here?")
            }
        }
    }
}
fun description(commandList: List<CommandData>, event: StringSelectInteractionEvent, callback: (String) -> Unit)
{
    event.guild?.retrieveCommands()?.queue({ commands ->
        if (commands.isEmpty())
        {
            callback("""
                There are no slash commands available in this guild.
                To add commands, send a message mentioning ${event.jda.selfUser.asTag} and saying \"setup\" (not case sensitive)
                """.trimIndent())
        }
        else
        {
            val description = StringBuilder()
            val allowedCommands = commands
                .filter{ command ->
                    commandList
                        .any{
                            command.name == it.name
                        }
                }
            for (command in allowedCommands)
            {
                description.append("${command.asMention}\n${command.description} \n")
            }
            callback(description.toString())
        }
    }, {
        callback("Failed to retrieve commands.")
    })
}