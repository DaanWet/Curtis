package com.cortexdevelopment.curtis.core.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Help extends Command {

    private ArrayList<Command> commands;

    public Help(){
        name = "help";
        aliases = new String[]{"commands", "command", "h", "test"};
    }

    public void setCommands(ArrayList<Command> comm){
        commands = comm;
    }

    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Curtis Commands");
        Map<String, StringBuilder> sbs = new HashMap<>();
        for (Command c : commands) {
            if (c.getCategory() == null ||  !c.getCategory().equalsIgnoreCase("moderation")) {
                String cat = c.getCategory();
                if (!sbs.containsKey(cat)) {
                    sbs.put(cat, new StringBuilder());
                }
                StringBuilder sb = sbs.get(cat);
                String[] als = c.getAliases();
                sb.append(String.format("\n!%s%s", c.getName(), als.length > 0 ? "[" : ""));
                Arrays.stream(c.getAliases()).forEach(alias -> sb.append(alias).append(", "));
                if (als.length > 0){
                    sb.delete(sb.length() - 2, sb.length());
                }
                sb.append(String.format("%s: *%s*", als.length > 0 ? "]" : "" ,c.getDescription().trim()));
            }
        }
        eb.setColor(Color.ORANGE);
        sbs.keySet().forEach(s -> eb.addField(s, sbs.get(s).toString().trim(), false));
        eb.setFooter("This is still a WIP, more features coming soon");
        e.getChannel().sendMessage(eb.build()).queue();
    }

    @Override
    public String getDescription() {
        return "Shows this overview";
    }
}
