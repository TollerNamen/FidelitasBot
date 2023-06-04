package org.tollernamen.fidelitas

import org.tollernamen.fidelitas.connectToDiscord.Opcode
import java.io.File

val tokenFile = File("/home/admindavid/IdeaProjects/Token/FidelitasToken.txt")
val token = tokenFile.readText().trim()

enum class AnsiColor(val code: String) {
    RESET("\u001B[0m"),
    BLACK("\u001B[30m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m"),
    MAGENTA("\u001B[35m"),
    CYAN("\u001B[36m"),
    WHITE("\u001B[37m"),
    BRIGHT_BLACK("\u001B[30;1m"),
    BRIGHT_RED("\u001B[31;1m"),
    BRIGHT_GREEN("\u001B[32;1m"),
    BRIGHT_YELLOW("\u001B[33;1m"),
    BRIGHT_BLUE("\u001B[34;1m"),
    BRIGHT_MAGENTA("\u001B[35;1m"),
    BRIGHT_CYAN("\u001B[36;1m"),
    BRIGHT_WHITE("\u001B[37;1m"),
    BG_BLACK("\u001B[40m"),
    BG_RED("\u001B[41m"),
    BG_GREEN("\u001B[42m"),
    BG_YELLOW("\u001B[43m"),
    BG_BLUE("\u001B[44m"),
    BG_MAGENTA("\u001B[45m"),
    BG_CYAN("\u001B[46m"),
    BG_WHITE("\u001B[47m"),
    BG_BRIGHT_BLACK("\u001B[40;1m"),
    BG_BRIGHT_RED("\u001B[41;1m"),
    BG_BRIGHT_GREEN("\u001B[42;1m"),
    BG_BRIGHT_YELLOW("\u001B[43;1m"),
    BG_BRIGHT_BLUE("\u001B[44;1m"),
    BG_BRIGHT_MAGENTA("\u001B[45;1m"),
    BG_BRIGHT_CYAN("\u001B[46;1m"),
    BG_BRIGHT_WHITE("\u001B[47;1m")

    companion object
    {
        private val ansiColorMap = AnsiColor.values().associateBy(AnsiColor::code)
        fun fromCode(code: String) = ansiColorMap[code]
    }
}
