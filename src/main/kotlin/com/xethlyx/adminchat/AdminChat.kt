package com.xethlyx.adminchat

import net.md_5.bungee.api.plugin.Plugin
import com.imaginarycode.minecraft.redisbungee.RedisBungee
import com.imaginarycode.minecraft.redisbungee.RedisBungeeAPI
import com.xethlyx.adminchat.commands.AdminChatHandler
import net.luckperms.api.LuckPerms
import net.luckperms.api.LuckPermsProvider

class AdminChat: Plugin() {
    companion object {
        lateinit var instance: AdminChat
        lateinit var redisBungee: RedisBungeeAPI
        lateinit var luckPerms: LuckPerms
    }

    override fun onEnable() {
        super.onEnable()

        instance = this
        redisBungee = RedisBungee.getApi()
        luckPerms = LuckPermsProvider.get()

        ChatHandler.registerListeners()
        proxy.pluginManager.registerCommand(this, AdminChatHandler())
    }

    override fun onDisable() {
        super.onDisable()
        ChatHandler.unregisterListeners()
    }
}