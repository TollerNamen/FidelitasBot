package commands

import CommandHandler
import embedColor
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.utils.FileUpload
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO

val baos = ByteArrayOutputStream()
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
                .queue()
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
    }
}