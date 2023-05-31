package org.tollernamen.fidelitas.listeners

import org.tollernamen.fidelitas.addCommandsToGuild
import org.tollernamen.fidelitas.addSlashCmds
import org.tollernamen.fidelitas.guildIds
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class Ready : ListenerAdapter()
{
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