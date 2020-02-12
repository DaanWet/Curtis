package com.cortexdevelopment.curtis.core;

import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

/*
  https://discordapp.com/api/oauth2/authorize?client_id=610838255058944003&permissions=2093476945&scope=bot
 */
public class Main {

    private static RedditClient redditClient;


    public static void main(String[] args) throws Exception {
        UserAgent userAgent = new UserAgent("bot", "me.Damascus2000", "v0.1", "Damascus2000");
        Credentials credentials = Credentials.script(args[1], args[2], "r6tA4vZG-LUtsA", "myC1yQrfJJj5T4jMtbrGfqXLxqU");
        NetworkAdapter adapter = new OkHttpNetworkAdapter(userAgent);
        redditClient = OAuthHelper.automatic(adapter, credentials);
        JDA jda = new JDABuilder(args[0]).setActivity(Activity.listening("!help")).build();
        jda.addEventListener(new MessageListener());
    }


    public static RedditClient getRedditClient(){
        return redditClient;
    }
}


