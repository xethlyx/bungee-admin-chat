package com.xethlyx.adminchat

import com.xethlyx.adminchat.listeners.RedisPubSub
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.hover.content.Text
import net.md_5.bungee.api.connection.ProxiedPlayer

object ChatHandler {
    private var redisPubSub = RedisPubSub()
    const val delimiter = "//"

    fun registerListeners() {
        AdminChat.redisBungee.registerPubSubChannels("adminchat:send")
        AdminChat.instance.proxy.pluginManager.registerListener(AdminChat.instance, redisPubSub)
    }

    fun unregisterListeners() {
        AdminChat.instance.proxy.pluginManager.unregisterListener(redisPubSub)
    }

    fun sendMessage(sender: ProxiedPlayer, message: String) {
        AdminChat.instance.proxy.scheduler.runAsync(AdminChat.instance) {
            val user = AdminChat.luckPerms.getPlayerAdapter(ProxiedPlayer::class.java).getUser(sender)
            val metaData = user.cachedData.metaData

            val displayName = ChatColor.translateAlternateColorCodes('&', metaData.prefix) +
                    sender.name +
                    ChatColor.translateAlternateColorCodes('&', metaData.suffix)

            val server = sender.server.info.name

            sendMessage(displayName, server, message)
        }
    }

    fun sendMessage(sender: CommandSender, message: String) {
        if (sender is ProxiedPlayer) return sendMessage(sender, message)

        AdminChat.instance.proxy.scheduler.runAsync(AdminChat.instance) {
            sendMessage(
                ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "CONSOLE",
                "RedisBungee", message
            )
        }
    }

    private fun sendMessage(displayName: String, server: String, message: String) {
        AdminChat.redisBungee.sendChannelMessage(
            "adminchat:send",
            "$displayName$delimiter$server$delimiter$message"
        )
    }

    fun formatMessageAndBroadcast(displayName: String, server: String, message: String) {
        val prefixHoverText = Text(
            ChatColor.DARK_AQUA.toString() + "Admin Chat\n" +
            ChatColor.GRAY.toString() + "Private admin only chat"
        )

        val prefixComponent = TextComponent()
        prefixComponent.text = "[A] "
        prefixComponent.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, prefixHoverText)
        prefixComponent.clickEvent = ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ac ")
        prefixComponent.color = ChatColor.DARK_AQUA

        val senderHoverText = Text(
            displayName + "\n" +
            ChatColor.GRAY.toString() + "Server" + ChatColor.DARK_GRAY.toString() + ": " +
            ChatColor.GRAY.toString() + server
        )

        val senderComponent = TextComponent()
        senderComponent.text = displayName
        senderComponent.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, senderHoverText)

        val splitComponent = TextComponent()
        splitComponent.text = " \u00bb "
        splitComponent.color = ChatColor.DARK_GRAY

        val playerMessageComponent = TextComponent()
        playerMessageComponent.text = message
        playerMessageComponent.color = ChatColor.DARK_AQUA

        for (player in AdminChat.instance.proxy.players) {
            if (!player.hasPermission("xethlyx.adminchat.see")) continue
            player.sendMessage(prefixComponent, senderComponent, splitComponent, playerMessageComponent)
        }

        val console = AdminChat.instance.proxy.console
        console.sendMessage(prefixComponent, senderComponent, splitComponent, playerMessageComponent)
    }
}
