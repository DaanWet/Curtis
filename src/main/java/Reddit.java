import net.dean.jraw.RedditClient;
import net.dean.jraw.models.*;
import net.dean.jraw.pagination.BarebonesPaginator;
import net.dean.jraw.pagination.DefaultPaginator;
import net.dean.jraw.references.SubredditReference;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.pagination.MessagePaginationAction;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.previous;

public class Reddit extends ListenerAdapter {

    private RedditClient redditClient;
    private Message message;

    public Reddit(RedditClient redditClient) {
        this.redditClient = redditClient;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        String[] words = e.getMessage().getContentRaw().split(" ");
        if (words.length == 1 && words[0].equalsIgnoreCase("!reddit")) {
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
        else if (words.length == 2 && words[0].equalsIgnoreCase("!amount")) {
            //e.getChannel().sendMessage(String.format("Now: %s \n Sunday two weeks ago: %s", e.getMessage().getCreationTime().toLocalDate().toString(), LocalDate.now().with(previous(SUNDAY)).minusDays(7))).queue();
            int weekone = 0;
            int weektwo = 0;
            //int ch = 0;
            //List<TextChannel> channels = e.getChannel().getGuild().getTextChannels();
            e.getChannel().sendMessage("Calculating :hourglass_flowing_sand: this may take a while").queue(this::setMessage);
            Member user =  e.getChannel().getGuild().getMemberById(words[1]);
            LocalDate twosunday = LocalDate.now().with(previous(SUNDAY)).minusDays(7);
            LocalDate sunday = LocalDate.now().with(previous(SUNDAY));
                for (TextChannel ch : e.getChannel().getGuild().getTextChannels()) {
                    MessagePaginationAction mpa = ch.getIterableHistory();
                    for (Message message : mpa) {
                        if (message.getMember() == user) {
                            LocalDate messagedate = message.getTimeCreated().toLocalDate();
                            if ((messagedate.isAfter(twosunday) && messagedate.isBefore(sunday)) || messagedate.isEqual(sunday)) {
                                weekone++;
                            } else if (messagedate.isAfter(sunday)) {
                                weektwo++;
                            }
                        }
                    }
                    //ch++;
                    System.out.println(ch.getName());
                }

            e.getChannel().sendMessage("Amount of messages sent by :" +  user.getAsMention() + String.format("\nPrevious week: %d\nThis week: %d", weekone, weektwo)).queue();
            e.getMessage().delete().queue();
            message.delete().queue();
        }
        else if (words.length == 1 && words[0].equalsIgnoreCase("!help")){
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.orange);
            eb.setTitle("Curtis Commands");
            eb.addField("!reddit", "Gets you some plugin ideas (pulled from r/minecraftsuggestions)", false);
            eb.addField("!help", "Shows this overview", false);
            eb.setFooter("This is still a WIP, more features coming soon");
            e.getChannel().sendMessage(eb.build()).queue();
        }
    }

    public void setMessage(Message m){
        message = m;
    }
}
