package commands

import CommandHandler
import embedColor
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class HelpCommandHandler : CommandHandler
{
    override fun handle(event: SlashCommandInteractionEvent)
    {
        val embedBuilder = EmbedBuilder()
            .setTitle("Help")
            .setColor(embedColor)

        event.guild?.retrieveCommands()?.queue{
            if (it.isEmpty())
            {
                embedBuilder.setDescription("There are no slash commands available in this guild. To add commands, send a message mentioning ${event.jda.selfUser.asTag} and saying \"setup\" (not case sensitive)")
            }
            else
            {
                embedBuilder.setDescription(it.joinToString(separator = "\n") { command ->
                    val commandMention = command.asMention
                    val commandDescription = command.description
                    "• $commandMention\n  $commandDescription"
                }
                )
            }
        }
        event.hook.sendMessageEmbeds(embedBuilder.build()).queue()
    }
}