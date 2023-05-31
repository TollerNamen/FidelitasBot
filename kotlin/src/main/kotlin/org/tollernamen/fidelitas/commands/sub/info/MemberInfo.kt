package org.tollernamen.fidelitas.commands.sub.info

import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.utils.FileUpload
import org.tollernamen.fidelitas.*
import org.tollernamen.fidelitas.misc.ImageImageElement
import org.tollernamen.fidelitas.misc.ImageProperties
import org.tollernamen.fidelitas.misc.ImageTextElement
import org.tollernamen.fidelitas.misc.imageCreator
import java.awt.Color
import java.awt.Font
import java.time.format.DateTimeFormatter
class MemberInfoHandler : SubCommandHandler
{
    private fun getMember(event: SlashCommandInteractionEvent): Member?
    {
        return when (event.options.size) {
            1 -> event.member
            else -> event.getOption("user")?.asMember
        }
    }
    override fun handle(event: SlashCommandInteractionEvent)
    {
        try
        {
            val member = getMember(event)
            val highestRoleName = member?.roles?.get(0)?.name
            val formattedDateTime = DateTimeFormatter.ofPattern("dd. LLL. yyyy")
            val joined: String? = member?.timeJoined?.toLocalDateTime()?.format(formattedDateTime)

            val imageTextElementName =
                member?.user?.name?.let { ImageTextElement(300, 125, Font("Arial", Font.BOLD, 120), Color.WHITE, it, 975.0f) }

            val imageTextElementJoined =
                ImageTextElement(300, 200, Font("Arial", Font.BOLD, 45), Color.WHITE, "Has Joined: $joined", 975.0f)

            val imageTextElementHighestRoleName =
                ImageTextElement(300, 275, Font("Arial", Font.BOLD, 45), Color.WHITE, "Highest Role: $highestRoleName", 975f)

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
}