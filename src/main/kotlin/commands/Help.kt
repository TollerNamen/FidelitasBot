package commands

import imagePreparer
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.utils.FileUpload

fun help(event: SlashCommandInteractionEvent)
{
    val name = event.jda.selfUser.name
    val imageBytes = imagePreparer(event.jda.selfUser.effectiveAvatarUrl, "About $name",
        """
        Fidelitas is an open source project licensed under the GNU General Public License.
        The Programming language used is kotlin, while it uses the JDA-API-WRAPPER.
        It also uses the build automation tool gradle.
        """.trimIndent())

    // Build the Discord Embed message with the image as an attachment
    val embedBuilder = EmbedBuilder()
    embedBuilder.setTitle("Help")
    embedBuilder.setImage("attachment://image.png")
    embedBuilder.setColor(0x040791)
    val embed: MessageEmbed = embedBuilder.build()

    event.hook.sendMessageEmbeds(embed).addFiles(FileUpload.fromData(imageBytes, "image.png")).queue()
}