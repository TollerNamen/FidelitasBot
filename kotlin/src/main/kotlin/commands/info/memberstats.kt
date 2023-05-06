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
            else -> event.getOption("user")?.asMember
        }
        val highestRoleName = member?.roles?.get(0)?.name
        val formattedDateTime = DateTimeFormatter.ofPattern("dd. LLL. yyyy")
        val joined: String? = member?.timeJoined?.toLocalDateTime()?.format(formattedDateTime)

        val imageTextElementName =
            member?.user?.name?.let { ImageTextElement(300, 125, Font("Arial", Font.BOLD, 120), Color.WHITE, it, 975) }

        val imageTextElementJoined =
            ImageTextElement(300, 200, Font("Arial", Font.BOLD, 45), Color.WHITE, "Has Joined: $joined", 975)

        val imageTextElementHighestRoleName =
            ImageTextElement(300, 275, Font("Arial", Font.BOLD, 45), Color.WHITE, "Highest Role: $highestRoleName", 975)

        val imageImageElement = ImageImageElement(25, 25, Color.WHITE, 5, 250, 250, member?.effectiveAvatarUrl, null)

        val imageProperties = ImageProperties(
            1300,
            300,
            "background.jpg",
            listOf(imageTextElementName, imageTextElementJoined, imageTextElementHighestRoleName),
            listOf(imageImageElement)
        )

        event.hook.sendFiles(FileUpload.fromData(imageCreator(imageProperties), "image.jpg")).queue()
    }
    catch (e: Exception)
    {
        event.hook.sendMessage("Error: ${e.message}")
        e.printStackTrace()
    }
}