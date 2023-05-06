package org.tollernamen.fidelitas

import java.io.File

val tokenFile = File("/home/admindavid/IdeaProjects/Token/FidelitasToken.txt")
val token = tokenFile.readText().trim()