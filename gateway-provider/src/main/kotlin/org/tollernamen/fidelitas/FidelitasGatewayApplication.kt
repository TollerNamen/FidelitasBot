package org.tollernamen.fidelitas

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.tollernamen.fidelitas.connectToDiscord.DiscordGateway
import org.tollernamen.fidelitas.connectToDiscord.listener
import org.tollernamen.fidelitas.websocketserver.chatHandler

val discordGateway = DiscordGateway("wss://gateway.discord.gg/?v=8&encoding=json")

@SpringBootApplication
class FidelitasGatewayApplication

fun main(args: Array<String>)
{
	discordGateway.connect(listener)

	// Add a shutdown hook to gracefully close the connection
	Runtime.getRuntime().addShutdownHook(Thread {
		discordGateway.close()
		chatHandler.closeOnShutDown()
	})
	// Keep the main thread alive to receive messages
	//readlnOrNull()

	runApplication<FidelitasGatewayApplication>(*args)
}