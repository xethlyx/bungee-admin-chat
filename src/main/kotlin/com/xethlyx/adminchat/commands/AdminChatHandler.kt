package com.xethlyx.adminchat.commands

import com.xethlyx.adminchat.ChatHandler
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command

class AdminChatHandler: Command("adminchat") {
    override fun execute(sender: CommandSender, args: Array<out String>) {
        ChatHandler.sendMessage(sender, args.joinToString(" "))
    }
}