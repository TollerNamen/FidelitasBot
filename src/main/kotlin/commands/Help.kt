package commands

import embedColor
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
fun help(event: SlashCommandInteractionEvent)
{
    /* archive
    // Build the Discord message with the image as an attachment
    val message = "Help"
    val file = File("image.png")
    file.writeBytes(imageBytes)
    event.hook.sendMessage(message).addFiles(FileUpload.fromData(imageBytes, "image.png")).queue()
     */

    val embedBuilder = EmbedBuilder()
        .setTitle("Available Commands")
        .setDescription("""
            • /help
            • /statistics
        """.trimIndent())
        .setColor(embedColor)

    event.hook.sendMessageEmbeds(embedBuilder.build()).queue()
}