package commands

import embedColor
import imagePreparer
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.utils.FileUpload
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder
import java.io.File

fun help(event: SlashCommandInteractionEvent)
{
    val name = event.jda.selfUser.name
    val imageBytes = imagePreparer(event.jda.selfUser.effectiveAvatarUrl, name,
        "A multi purpose open source Discord bot",
        "background.jpg")
    /* archive
    // Build the Discord message with the image as an attachment
    val message = "Help"
    val file = File("image.png")
    file.writeBytes(imageBytes)
    event.hook.sendMessage(message).addFiles(FileUpload.fromData(imageBytes, "image.png")).queue()
     */

    // Build the Discord Embed message with the image as an attachment
    val embedBuilder1 = EmbedBuilder()
        .setTitle("Available Commands")
        .setDescription("""
            • /help
            • /statistics
        """.trimIndent())
        .setColor(embedColor)
    val embedBuilder2 = EmbedBuilder()
        .setTitle("About $name")
        .setDescription(
            """
            $name originates from the open source Fidelitas project on GitHub.
            
            • [GitHub](https://github.com/TollerNamen/FidelitasBot)
            • [FidelitasBot Community](https://discord.gg/EcbnGTSMZZ)
            • [Add FidelitasBot to your Server](https://discord.com/api/oauth2/authorize?client_id=1000390823273304066&permissions=8&scope=bot)
            
        """.trimIndent())
        .setImage("attachment://image.png")
        .setColor(embedColor)
    val messageCreate: MessageCreateBuilder = MessageCreateBuilder().setEmbeds(embedBuilder1.build(), embedBuilder2.build())
    val message = messageCreate.build()

    val file = File("image.png")
    file.writeBytes(imageBytes)
    event.hook.sendMessage(message).addFiles(FileUpload.fromData(imageBytes, "image.png")).queue()
}