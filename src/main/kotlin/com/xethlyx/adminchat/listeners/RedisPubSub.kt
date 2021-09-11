package com.xethlyx.adminchat.listeners

import com.imaginarycode.minecraft.redisbungee.events.PubSubMessageEvent
import com.xethlyx.adminchat.ChatHandler
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import java.util.*

class RedisPubSub: Listener {
    @EventHandler
    fun pubSubMessageEvent(event: PubSubMessageEvent) {
        when (event.channel) {
            "adminchat:send" -> {
                val messageContent = event.message.split(ChatHandler.delimiter).toMutableList()

                val displayName = messageContent.removeFirst()
                val server = messageContent.removeFirst()
                val contents = messageContent.joinToString(ChatHandler.delimiter)

                ChatHandler.formatMessageAndBroadcast(displayName, server, contents)
            }
        }
    }
}