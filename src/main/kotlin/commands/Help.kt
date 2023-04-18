package commands

import embedColor
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu
import net.dv8tion.jda.api.utils.FileUpload
import java.io.File
import javax.imageio.ImageIO

fun help(event: SlashCommandInteractionEvent)
{
    /* archive
    // Build the Discord message with the image as an attachment
    val message = "Help"
    val file = File("image.png")
    file.writeBytes(imageBytes)
    event.hook.sendMessage(message).addFiles(FileUpload.fromData(imageBytes, "image.png")).queue()
     */
    /*
    val name = event.jda.selfUser.name
    val embedBuilder = EmbedBuilder()
        .setTitle("About $name")
        .setDescription(
            """
            $name originates from the open source Fidelitas project on GitHub.

            • [GitHub](https://github.com/TollerNamen/FidelitasBot)
            • [Community](https://discord.gg/EcbnGTSMZZ)
            • [Invite](https://discord.com/api/oauth2/authorize?client_id=1000390823273304066&permissions=8&scope=bot)
            """.trimIndent()
        )
        .setImage("attachment://about.png")
        .setColor(embedColor)

    val file = File("about.png")

    try
    {
        val bufferedImage = ImageIO.read(file)

        // Convert the image to a byte array for use as an attachment in the Discord Embed message
        ImageIO.write(bufferedImage, "png", baos)

        event.hook.sendMessageEmbeds(embedBuilder.build())
            .addFiles(FileUpload.fromData(baos.toByteArray(), "about.png"))
            .addActionRow(StringSelectMenu.create("help").addOption("General", "general", "general commands").build())
            .queue(event -> {})
    }
    catch (e: Exception)
    {
        event.hook.sendMessage("Error ${e.message}")
        e.printStackTrace()
    }
    finally
    {
        baos.close()
    }


     */
    val embedBuilder = EmbedBuilder()
        .setTitle("Available Commands")
        .setDescription("""
            • **/about**
              Explanation about a project
            • **/help**
              Shows Available Commands
            • **/statistics**
              Statistics about a user or the server
        """.trimIndent())
        .setColor(embedColor)

    event.hook.sendMessageEmbeds(embedBuilder.build()).queue()
}