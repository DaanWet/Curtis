package com.cortexdevelopment.curtis.core.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.pagination.MessagePaginationAction;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.time.DayOfWeek.SUNDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static java.time.temporal.TemporalAdjusters.previous;

public class MessageCount extends Command {

    private Message message;
    public MessageCount(){
        name = "amount";
        aliases = new String[]{"mcount", "messagecount"};
        category = "MODERATION";
    }

    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        String m = e.getMessage().getContentRaw();
        String[] words = m.split(" ");
        int thisw = 0;
        int prevw = 0;
        int twoprevw = 0;
        e.getChannel().sendMessage("Calculating :hourglass_flowing_sand: this may take a while.").queue(this::setMessage);
        Member user =  e.getChannel().getGuild().getMemberById(words[1]);
        LocalDate sunday = LocalDate.now().with(previous(SUNDAY));
        LocalDate twosunday = sunday.minusDays(7);
        LocalDate thrsunday  = twosunday.minusDays(7);
        for (TextChannel ch : e.getChannel().getGuild().getTextChannels()) {
            MessagePaginationAction mpa = ch.getIterableHistory();
            for (Message message : mpa) {
                if (message.getMember() == user) {
                    LocalDate messagedate = message.getTimeCreated().toLocalDate();
                    if ((messagedate.isAfter(thrsunday) && messagedate.isBefore(twosunday)) || messagedate.isEqual(twosunday)){
                        twoprevw++;
                    } else if ((messagedate.isAfter(twosunday) && messagedate.isBefore(sunday)) || messagedate.isEqual(sunday)) {
                        prevw++;
                    } else if (messagedate.isAfter(sunday)) {
                        thisw++;
                    }
                }
            }
            //ch++;
            System.out.println(ch.getName());
        }

        e.getChannel().sendMessage("Amount of messages sent by :" +  user.getAsMention() + String.format("\nTwo weeks ago: %d\nPrevious week: %d\nThis week: %d", twoprevw, prevw, thisw)).queue();
        e.getMessage().delete().queue();
        message.delete().queue();
    }

    @Override
    public String getDescription() {
        return "Counts amount of message of certain member";
    }

    public void setMessage(Message m){
        message = m;
    }
}
