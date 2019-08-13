import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
/*
  https://discordapp.com/api/oauth2/authorize?client_id=610838255058944003&permissions=2093476945&scope=bot
 */
public class Main {

    public static void main(String[] args) throws Exception {
        JDA jda = new JDABuilder(args[0]).build();
        UserAgent userAgent = new UserAgent("bot", "me.Damascus2000", "v0.1", "Damascus2000");
        Credentials credentials = Credentials.script(args[1], args[2], "r6tA4vZG-LUtsA", "myC1yQrfJJj5T4jMtbrGfqXLxqU");
        NetworkAdapter adapter = new OkHttpNetworkAdapter(userAgent);
        RedditClient reddit = OAuthHelper.automatic(adapter, credentials);
        jda.addEventListener(new Reddit(reddit));
    }
}
