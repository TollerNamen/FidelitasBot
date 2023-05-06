import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

fun pyAndJsIntegrater(event: SlashCommandInteractionEvent, fileName: String, args: String?)
{
    val file = File(fileName)

    val runTimeCmd = when (file.extension)
    {
        "js" -> "node"
        "py" -> "python"
        else -> {
            println("Error: File extension not accepted right now")
            return
        }
    }

    println("Starting File execution")

    val process = Runtime.getRuntime().exec("$runTimeCmd PythonAndJS/$fileName $args")
    val inputStream = process.inputStream
    val bufferedReader = BufferedReader(InputStreamReader(inputStream))
    val outputUnParsed = StringBuilder()

    println("File execution ended")

    // Read the output stream
    while (true) {
        val line = bufferedReader.readLine() ?: break
        outputUnParsed.append(line)
    }
    println(outputUnParsed)

    println("File process ended")

    val input: String = outputUnParsed.toString()

    println(input)

    val split = input.split(", ")
    val map = mutableMapOf<String, String>()
    for (word in split)
    {
        val (key, value) = word.split(":")
        map.put(key.trim(), value.trim())
    }
    println(map)

    println("Parsed successfully")

    val title = map["title"]
    val description = map["description"]

    // Parse the output string and create a Discord Embed
    val embed = EmbedBuilder()
        .setTitle(title)
        .setDescription("${description}")
        .setColor(embedColor)
        .build()

    event.hook.sendMessageEmbeds(embed).queue {
        println("Message sent!")
    }
}
