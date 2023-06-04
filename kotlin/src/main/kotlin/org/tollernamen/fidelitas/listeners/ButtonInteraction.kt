package org.tollernamen.fidelitas.listeners

import org.tollernamen.fidelitas.addSlashCmds
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.ActionRow

class ButtonInteraction : ListenerAdapter()
{
    override fun onButtonInteraction(event: ButtonInteractionEvent)
    {
        if (event.button.id == "addSlashCmds")
        {
            event.deferReply().queue()
            event.guild?.let { addSlashCmds(it.id, true, event) }

            val pressedButton = event.button
            val disabledButton = pressedButton.withDisabled(true)

            val buttons = event.message.actionRows[0].buttons.toMutableList()

            val buttonIndex = buttons.indexOf(pressedButton)

            buttons[buttonIndex] = disabledButton

            val newRow = ActionRow.of(buttons)

            event.message.editMessageComponents(newRow).queue()
        }
    }
}