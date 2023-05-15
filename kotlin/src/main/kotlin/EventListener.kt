import commands.baos
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.utils.FileUpload
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO

class EventListener : ListenerAdapter()
{
    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent)
    {
        event.deferReply(false).queue()
        AssignHandlersToInteraction(event)
    }
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
    override fun onButtonInteraction(event: ButtonInteractionEvent)
    {
        if (event.button.id == "addSlashCmds")
        {
            event.deferReply().queue()
            event.guild?.let { addSlashCmds(it.id, true, event) }
            val pressedButton = event.button
            val disabledButton = pressedButton.withDisabled(true)
            val row = event.message.actionRows[0]
            val buttons = row.buttons.toMutableList()
            val buttonIndex = buttons.indexOf(pressedButton)
            buttons[buttonIndex] = disabledButton
            val newRow = ActionRow.of(buttons)
            event.message.editMessageComponents(newRow).queue()
        }
    }
    override fun onStringSelectInteraction(event: StringSelectInteractionEvent)
    {
        if (event.selectMenu.id == "commandBrowser")
        {
            event.deferEdit().queue()
            println("stringMenu Int ack")

            val newEmbed = EmbedBuilder().setColor(embedColor)

            when (event.values[0]) {
                "help_general" -> {
                    println("general chosen")
                    description(commandData, event) {
                        newEmbed.setTitle("Help - General Commands")
                            .setDescription(it)
                            .setImage("attachment://about.png")

                        val file = File("about.png")
                        try
                        {
                            val bufferedImage = ImageIO.read(file)
                            val baos = ByteArrayOutputStream()
                            ImageIO.write(bufferedImage, "png", baos)
                            event.message.editMessageEmbeds(newEmbed.build()).queue()
                        }
                        catch (e: Exception)
                        {
                            e.printStackTrace()
                        }
                        finally {
                            baos.close()
                        }
                        //event.message.editMessageEmbeds(newEmbed.build()).queue()
                    }
                }
                "help_about" -> {
                    println("about chosen")
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
                        .setImage("attachment://about.png")

                    val file = File("about.png")

                    try
                    {
                        val bufferedImage = ImageIO.read(file)
                        val baos = ByteArrayOutputStream()
                        ImageIO.write(bufferedImage, "png", baos)
                        event.message.editMessageEmbeds(newEmbed.build()).queue()
                    }
                    catch (e: Exception)
                    {
                        e.printStackTrace()
                    }
                    finally
                    {
                        baos.close()
                    }
                }
                else -> event.reply("How did you get here?")
            }
        }

    }
    override fun onReady(event: ReadyEvent)
    {
        if (addCommandsToGuild)
        {
            println("Adding Commands to guilds is turned on:")
            for (guildId in guildIds)
            {
                addSlashCmds(guildId, false, null)
            }
        }
        else
        {
            println("Adding Commands to guilds is turned off.")
        }
    }
}
fun description(commandList: List<CommandData>, event: StringSelectInteractionEvent, callback: (String) -> Unit)
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
                    commandList
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