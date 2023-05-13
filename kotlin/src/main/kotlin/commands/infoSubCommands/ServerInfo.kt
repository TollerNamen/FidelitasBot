package commands.infoSubCommands

import ImageImageElement
import ImageProperties
import ImageTextElement
import SubCommandHandler
import imageCreator
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.utils.FileUpload
import java.awt.Color
import java.awt.Font
import java.time.format.DateTimeFormatter

class ServerInfoHandler : SubCommandHandler
{
    override fun handle(event: SlashCommandInteractionEvent)
    {
        try
        {
            val guild: Guild? = event.guild
            val memberCount = guild?.memberCount
            val formattedDateTime = DateTimeFormatter.ofPattern("dd. LLL. yyyy")
            val created: String? = guild?.timeCreated?.toLocalDateTime()?.format(formattedDateTime)

            val imageTextElementName =
                guild?.name?.let { ImageTextElement(300, 125, Font("Arial", Font.BOLD, 120), Color.WHITE, it, 975f) }

            val imageTextElementTimeCreated =
                ImageTextElement(300, 200, Font("Arial", Font.BOLD, 45), Color.WHITE, "Time Created: $created", 975f)

            val imageTextElementMemberCount =
                ImageTextElement(300, 275, Font("Arial", Font.BOLD, 45), Color.WHITE, "Member-count: $memberCount", 975f)

            val imageImageElement = ImageImageElement(25, 25, Color.WHITE, 5, 250, 250, guild?.iconUrl, null)

            val imageProperties = ImageProperties(
                1300,
                300,
                "background.jpg",
                listOf(imageTextElementName, imageTextElementTimeCreated, imageTextElementMemberCount),
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
}