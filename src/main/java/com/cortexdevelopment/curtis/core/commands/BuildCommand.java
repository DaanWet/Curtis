package com.cortexdevelopment.curtis.core.commands;

import com.cortexdevelopment.curtis.core.Main;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class BuildCommand extends Command {


    public BuildCommand(){
        name = "build";
        category = "Programming";
    }

    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        for (String s : args){
            System.out.println(s);
        }
        if(args[0].equalsIgnoreCase("gradle")){
            e.getChannel().sendMessage("Stop using gradle, use maven you prick").queue();
        } else if(args[0].equalsIgnoreCase("maven")) {




            e.getChannel().sendMessage("```xml\n<dependency>\n" +
                    "  <groupId>net.dv8tion</groupId>\n" +
                    "  <artifactId>JDA</artifactId>\n" +
                    "  <version>4.1.1_135</version>\n" +
                    "  <type>pom</type>\n" +
                    "</dependency>```").queue();
        }
    }

    @Override
    public String getDescription() {
        return "Displays the gradle build";
    }
}
