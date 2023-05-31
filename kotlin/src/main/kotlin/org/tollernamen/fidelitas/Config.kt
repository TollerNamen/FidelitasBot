package org.tollernamen.fidelitas

import java.awt.Color
import java.io.File

val colors = mapOf<String, Color>(
    "blue.dark" to Color(0x040791),
    "blue.light" to Color.BLUE,
    "red.dark" to Color(0x910407),
    "red.light" to Color.RED,
    "yellow" to Color.YELLOW,
    "orange" to Color.ORANGE,
    "green" to Color.GREEN,
    "gray.dark" to Color.DARK_GRAY,
    "gray.light" to Color.LIGHT_GRAY
)

val tokenFile = File("/home/admindavid/IdeaProjects/Token/FidelitasToken.txt")
val guildIds: List<String> = listOf(/*"832328391347666964", "1059214543441637387", */"979105761084968960")
const val addCommandsToGuild = false
val defaultEmbedColor = colors["blue.dark"]
val aboutImageUrl = "https://camo.githubusercontent.com/986b8747c43d91868f6136a4af31bb729bfab0b5a51bd905721f23fdc11d2cea/68747470733a2f2f6d656469612e646973636f72646170702e6e65742f6174746163686d656e74732f3937393131303139363337323731333530322f313039333531373433383134333936373334322f696d6167652e706e67"