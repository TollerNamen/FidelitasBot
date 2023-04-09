#!/bin/sh

command=$1
args=$2
secondFileName=$3
filePath=$(pwd)/src/main/kotlin/commands/${command^}.kt

touch ${filePath}

echo -e "package commands\n" >> ${filePath}
echo -e "import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent" >> ${filePath}
echo -e "import pyAndJsIntegrater\n" >> ${filePath}
echo -e "fun $command(event: SlashCommandInteractionEvent)" >> ${filePath}
echo -e "{"  >> ${filePath}
echo -e "    // You need to enter the 'event' object, then your executable name and if you need, some arguments in the third parameter" >> ${filePath}
echo -e "    // This function executes your js or python file and retrives and sends the output of that file to Discord as a response" >> ${filePath}
echo -e "    pyAndJsIntegrater(event, \"$secondFileName\", $args)" >> ${filePath}
echo -e "    // For more Discord API interaction, you would need to execute kotlin code after the function here" >> ${filePath}
echo -e "}" >> ${filePath}

touch $(pwd)/PythonAndJS/$secondFileName
