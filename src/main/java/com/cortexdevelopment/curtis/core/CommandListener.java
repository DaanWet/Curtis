package com.cortexdevelopment.curtis.core;

import com.cortexdevelopment.curtis.core.commands.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class CommandListener {


    private final ArrayList<Command> commands;
    public CommandListener(){
        Help hc = new Help();
        commands = new ArrayList<>(Arrays.asList(
                new Reddit(), new MessageCount(), hc, new JavaDocs(), new BuildCommand()
        ));
        hc.setCommands(commands);
    }


    public void onCommandReceived(GuildMessageReceivedEvent e){
        String message = e.getMessage().getContentRaw();
        String[] words = message.split(" ");
        String[] parts = words[0].substring(1).split("\\.");
        for (Command c : commands){

            if (c.isCommandFor(parts[0])){
                String[] args = Stream.concat(Arrays.stream(parts, 1, parts.length), Arrays.stream(words, 1, words.length))
                        .toArray(String[]::new);
                c.run(args, e);
            }
        }
    }

}
