package commands.info

import ImageImageElement
import ImageProperties
import ImageTextElement
import imageCreator
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.utils.FileUpload
import java.awt.Color
import java.awt.Font
import java.time.format.DateTimeFormatter

fun memberstats(event: SlashCommandInteractionEvent)
{
    try {
        val member = when (event.options.size) {
            1 -> event.member
            else -> event.options[1].asMember
        }
        val formattedDateTime = DateTimeFormatter.ofPattern("dd. LLL. yyyy")
        val joined: String? = member?.timeJoined?.toLocalDateTime()?.format(formattedDateTime)

        val imageTextElementName =
            member?.user?.name?.let { ImageTextElement(300, 175, Font("Arial", Font.BOLD, 150), Color.WHITE, it) }
        val imageTextElementJoined =
            ImageTextElement(320, 250, Font("Arial", Font.BOLD, 45), Color.WHITE, "Has Joined: $joined")

        val imageImageElement = ImageImageElement(25, 25, Color.WHITE, 5, 250, 250, member?.effectiveAvatarUrl, null)

        val imageProperties = ImageProperties(
            1300,
            300,
            "background.jpg",
            listOf(imageTextElementName, imageTextElementJoined),
            listOf(imageImageElement)
        )

        event.hook.sendFiles(FileUpload.fromData(imageCreator(imageProperties), "image.jpg")).queue()
    }catch (e: Exception)
    {
        event.hook.sendMessage("Error: ${e.message}")
        e.printStackTrace()
    }
}