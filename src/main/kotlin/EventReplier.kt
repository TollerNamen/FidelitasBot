import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.hooks.ListenerAdapter

class EventReplier(
    private val interactmessage: MessageEmbed,
    private val listener: EventListener)
    : ListenerAdapter()
{

}