package com.cortexdevelopment.curtis.core.commands;


import com.cortexdevelopment.curtis.core.Main;
import net.dean.jraw.RedditClient;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.models.TimePeriod;
import net.dean.jraw.pagination.DefaultPaginator;
import net.dean.jraw.references.SubredditReference;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Iterator;

public class Reddit extends Command {


    private RedditClient redditClient;

    public Reddit(){
        name = "reddit";
        aliases = new String[]{"mcideas"};
        redditClient = Main.getRedditClient();
        category = "Programming";
    }


    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        SubredditReference srr = redditClient.subreddit("minecraftsuggestions");
        DefaultPaginator<Submission> paginator = srr.posts().sorting(SubredditSort.HOT).timePeriod(TimePeriod.DAY).limit(1000000000).build();
        Iterator<Submission> it = paginator.next().iterator();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Daily top 5 suggestions from reddit", "https://www.reddit.com/r/minecraftsuggestions");
        eb.setColor(5075791);
        eb.setDescription("Top 5 posts from reddit");
        int i = 0;
        while (it.hasNext()) {
            Submission submission = it.next();
            if (submission.getScore() >= 100) {
                if (i < 5) {
                    String text = submission.getSelfText().split("\n")[0].split("[image](https.*)")[0];
                    String descr = text.length() > 150 ? text.substring(0, 150) + "..." : text;
                    eb.addField("`^ " + submission.getScore() + " ¦ `" + submission.getTitle(), "```" + descr + "```[:link:](" + submission.getUrl() + ") - Submitted by *" + submission.getAuthor() + "*", false);
                }
                // Add it to the database
                i++;
            }
        }
        e.getChannel().sendMessage(eb.build()).queue();
    }

    @Override
    public String getDescription() {
        return "Gets you some plugin ideas (pulled from r/minecraftsuggestions)";
    }
}
