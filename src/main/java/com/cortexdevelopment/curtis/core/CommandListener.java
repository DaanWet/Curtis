package com.cortexdevelopment.curtis.core;

import com.cortexdevelopment.curtis.core.commands.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandListener {


    private final ArrayList<Command> commands;
    public CommandListener(){
        Help hc = new Help();
        commands = new ArrayList<>(Arrays.asList(
                new Reddit(), new MessageCount(), hc, new JavaDocs()
        ));
        hc.setCommands(commands);
    }


    public void onCommandReceived(GuildMessageReceivedEvent e){
        String message = e.getMessage().getContentRaw();
        String[] words = message.split(" ");
        String command = words[0].substring(1);
        for (Command c : commands){
            if (c.isCommandFor(command)){
                c.run(Arrays.copyOfRange(words, 1, words.length), e);
            }
        }
    }

}
