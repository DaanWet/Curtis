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
        name = "Help";
        aliases = new String[]{"commands", "command", "h", "test"};
    }

    public void setCommands(ArrayList<Command> comm){
        commands = comm;
    }

    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        System.out.println("yeet");
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
                sb.append(String.format("\n!%s[", c.getName()));
                Arrays.stream(c.getAliases()).forEach(alias -> sb.append(alias).append(", "));
                sb.delete(sb.length() - 2, sb.length()).append(String.format("]: *%s*", c.getDescription()));
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
