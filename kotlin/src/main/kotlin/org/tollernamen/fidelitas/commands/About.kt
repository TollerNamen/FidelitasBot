package org.tollernamen.fidelitas.commands

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu
import org.tollernamen.fidelitas.CommandHandler
import org.tollernamen.fidelitas.aboutImageUrl
import org.tollernamen.fidelitas.defaultEmbedColor

class AboutCommandHandler : CommandHandler
{
    override fun handle(event: SlashCommandInteractionEvent)
    {
        val name = event.jda.selfUser.name
        val embedBuilder = EmbedBuilder()
            .setTitle("About $name")
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
            .setColor(defaultEmbedColor)

        val browseMenu = StringSelectMenu.create("commandBrowser")
            .setPlaceholder("Browse Commands")
            .addOption("General", "help_general", Emoji.fromFormatted("\uD83D\uDD30"))
            .addOption("About", "help_about", Emoji.fromFormatted("ℹ\uFE0F"))
            .setRequiredRange(1, 1)

        event.hook.sendMessageEmbeds(embedBuilder.build())
            .addActionRow(browseMenu.build())
            .queue()
    }
}