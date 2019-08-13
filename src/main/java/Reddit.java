import net.dean.jraw.RedditClient;
import net.dean.jraw.models.*;
import net.dean.jraw.pagination.BarebonesPaginator;
import net.dean.jraw.pagination.DefaultPaginator;
import net.dean.jraw.references.SubredditReference;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.Iterator;
import java.util.List;

public class Reddit extends ListenerAdapter {

    private RedditClient redditClient;

    public Reddit(RedditClient redditClient){
        this.redditClient = redditClient;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e){
        String[] words = e.getMessage().getContentRaw().split(" ");
        if (words.length == 1 && words[0].equalsIgnoreCase("!reddit")){
            SubredditReference srr = redditClient.subreddit("minecraftsuggestions");
            DefaultPaginator<Submission> paginator =  srr.posts().sorting(SubredditSort.HOT).timePeriod(TimePeriod.DAY).limit(1000000000).build();
            Iterator<Submission> it = paginator.next().iterator();
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Daily top 5 suggestions from reddit", "https://www.reddit.com/r/minecraftsuggestions");
            eb.setColor(5075791);
            eb.setDescription("Top 5 posts from reddit");
            int i = 0;
            while (it.hasNext()){
                Submission submission = it.next();
                if (submission.getScore() >= 100){
                    if (i < 5) {
                        String text = submission.getSelfText().split("\n")[0];
                        String descr = text.length() > 140 ? text.substring(0, 128) + "..." : text;
                        eb.addField("^ " + submission.getScore() + " ¦ " + submission.getTitle(), "```" + descr + "```Submitted by *" + submission.getAuthor() + "* - [webpage](" + submission.getUrl() + ")", false);
                    }
                    // Add it to the database
                    i++;
                }
            }
            e.getChannel().sendMessage(eb.build()).queue();

        }
    }
}
