package commands

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import pyAndJsIntegrater

fun pythonTest(event: SlashCommandInteractionEvent)
{
    println("Starting PythonTest")
    pyAndJsIntegrater(event, "pythonTest.py", null)
}