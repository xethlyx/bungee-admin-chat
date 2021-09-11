package com.xethlyx.adminchat.commands

import com.xethlyx.adminchat.ChatHandler
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.TabExecutor

class AdminChatHandler: Command("adminchat", "xethlyx.adminchat.speak", "ac", "a"), TabExecutor {
    override fun execute(sender: CommandSender, args: Array<out String>) {
        if (args.isEmpty()) {
            val messageComponent = TextComponent()
            messageComponent.text = "Incorrect usage. Usage: /adminchat <message>"
            messageComponent.color = ChatColor.RED

            sender.sendMessage(messageComponent)
            return
        }

        ChatHandler.sendMessage(sender, args.joinToString(" "))
    }

    override fun onTabComplete(sender: CommandSender, args: Array<out String>): Iterable<String> {
        return emptyArray<String>().asIterable()
    }
}