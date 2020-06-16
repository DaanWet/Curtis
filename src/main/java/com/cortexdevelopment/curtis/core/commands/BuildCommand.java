package com.cortexdevelopment.curtis.core.commands;

import com.cortexdevelopment.curtis.core.Main;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.json.JSONObject;


import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BuildCommand extends Command {

    private final String GET_URL = "https://api.bintray.com/packages/dv8fromtheworld/maven/JDA/";

    public BuildCommand(){
        name = "build";
        category = "Programming";
    }

    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        for (String s : args){
            System.out.println(s);
        }
        try {
            URL url = new URL(GET_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setUseCaches(true);
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK){
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                JSONObject jsonObject = new JSONObject(response.toString());
                String version = jsonObject.getString("latest_version");

                if(args[0].equalsIgnoreCase("gradle")){
                    e.getChannel().sendMessage(String.format("```implementation 'net.dv8tion:JDA:%s'```", version)).queue();
                } else if(args[0].equalsIgnoreCase("maven")) {
                    e.getChannel().sendMessage(String.format("```xml\n<dependency>\n" +
                            "  <groupId>net.dv8tion</groupId>\n" +
                            "  <artifactId>JDA</artifactId>\n" +
                            "  <version>%s</version>\n" +
                            "  <type>pom</type>\n" +
                            "</dependency>```", version)).queue();
                }
            }
        } catch (IOException exc){

        }






    }

    @Override
    public String getDescription() {
        return "Displays the gradle build";
    }
}
