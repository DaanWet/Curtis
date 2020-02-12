package com.cortexdevelopment.curtis.core.commands;

import com.cortexdevelopment.curtis.core.JavaDocs.JDocScraper;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class JavaDocs extends Command {

    public JavaDocs(){
        name = "docs";
        aliases = new String[]{"javadocs", "SpigotDocs", "JDocs"};
        category = "Programming";
    }


    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        JDocScraper jds = new JDocScraper(e.getChannel());
        if (args.length > 0) {
            try{
                jds.parse(args[0]);
            } catch (Exception exc){
                e.getChannel().sendMessage("Woops, something has gone wrong").queue();
                exc.printStackTrace();
            }
        } else {
            jds.parseSummary();
        }
    }

    @Override
    public String getDescription() {
        return "Search javadocs ";
    }
}
