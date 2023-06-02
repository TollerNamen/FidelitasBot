package org.tollernamen.fidelitas

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.tollernamen.fidelitas.connectToDiscord.DiscordGateway
import org.tollernamen.fidelitas.connectToDiscord.listener
import org.tollernamen.fidelitas.connectToDiscord.standardGatewayUrl
import org.tollernamen.fidelitas.websocketserver.chatHandler

val discordGateway = DiscordGateway(standardGatewayUrl, listener)

@SpringBootApplication
class FidelitasGatewayApplication

fun main(args: Array<String>)
{
	discordGateway.connect()

	// Add a shutdown hook to gracefully close the connection
	Runtime.getRuntime().addShutdownHook(Thread {
		discordGateway.close()
		chatHandler.closeOnShutDown()
	})

	runApplication<FidelitasGatewayApplication>(*args)
}