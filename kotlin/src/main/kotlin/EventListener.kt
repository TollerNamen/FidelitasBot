import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.buttons.Button

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
    override fun onButtonInteraction(event: ButtonInteractionEvent) {
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