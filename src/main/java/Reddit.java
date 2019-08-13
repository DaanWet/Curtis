import net.dean.jraw.RedditClient;
import net.dean.jraw.models.Subreddit;
import net.dean.jraw.references.SubredditReference;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Reddit extends ListenerAdapter {

    private SubredditReference srr;

    public Reddit(RedditClient redditClient){
        srr = redditClient.subreddit("minecraftsuggestions");
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e){
        String[] words = e.getMessage().getContentRaw().split(" ");
        if (words.length == 1 && words[0].equalsIgnoreCase("!reddit")){
            srr.comments().
        }
    }
}
