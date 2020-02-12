package com.cortexdevelopment.curtis.core;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

    private CommandListener commandListener = new CommandListener();

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e){
        String message = e.getMessage().getContentRaw();
        if (message.length() > 0 && message.charAt(0) == '!'){
            commandListener.onCommandReceived(e);
        }
    }
}
