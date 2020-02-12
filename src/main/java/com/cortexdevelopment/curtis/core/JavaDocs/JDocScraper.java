package com.cortexdevelopment.curtis.core.JavaDocs;

import com.cortexdevelopment.curtis.core.JavaDocs.Util.JDocConstructor;
import com.cortexdevelopment.curtis.core.JavaDocs.Util.JDocLink;
import com.cortexdevelopment.curtis.core.JavaDocs.Util.JDocMethod;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeFilter;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class JDocScraper {

    private TextChannel channel;

    public JDocScraper(TextChannel t) {
        channel = t;
    }

    public void parseSummary() {
        try {
            Document doc = Jsoup.connect("https://hub.spigotmc.org/javadocs/spigot/overview-summary.html").get();

            Element summary = doc.getElementsByClass("overviewSummary").first();
            Elements packages = summary.getElementsByClass("colFirst");
            StringBuilder sb = new StringBuilder();
            int i = 0;
            int j = 0;
            while (j < 10 && i < packages.size()) {
                Element pack = packages.get(i);
                if (pack.attr("scope").equals("row")) {
                    sb.append(pack.text()).append("\n");
                    j++;
                }
                i++;
            }
            channel.sendMessage(new EmbedBuilder().setTitle(doc.title(), "https://hub.spigotmc.org/javadocs/spigot/overview-summary.html").setFooter("This is a WIP, errors could occur").addField("Packages", sb.append("...").toString(), false).build()).queue();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String translateLink(String URL) {
        return URL.replaceFirst(".*org/bukkit/", "https://hub.spigotmc.org/javadocs/spigot/org/bukkit/");
    }


    public void parse(String search) {
        try {
            Document doc = Jsoup.connect("https://hub.spigotmc.org/javadocs/spigot/overview-tree.html").get();
            Elements typeNameLinks = doc.getElementsByClass("typeNameLink");
            int i = 0;
            boolean found = false;
            while (!found && i < typeNameLinks.size()) {
                Element clas = typeNameLinks.get(i);
                if (clas.text().equalsIgnoreCase(search)) {
                    String path = clas.parent().parent().text().split(" ")[0];
                    String url = translateLink(clas.parent().attr("href"));
                    EmbedBuilder eb = parseClass(url);
                    eb.setTitle(path, url);
                    channel.sendMessage(eb.build()).queue();
                    found = true;
                }
                i++;
            }
            if (!found) {
                channel.sendMessage("No Such Class Found").queue();
            }

        } catch (IOException e) {
            channel.sendMessage("Something went wrong, please contact staff");
            e.printStackTrace();
        }
    }

    public EmbedBuilder parseClass(String URL) throws IOException {

        Document doc = Jsoup.connect(URL).get();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.ORANGE);
        Elements summaryEl = doc.getElementsByClass("memberSummary");
        for (Element summEl : summaryEl) {
            Element aElement = summEl.parent().getElementsByTag("a").first();
            System.out.println(aElement.attr("name"));
            switch (aElement.attr("name")) {
                case "method.summary":
                    ArrayList<JDocMethod> methods = parseMethods(summEl);
                    StringBuilder sb = new StringBuilder();
                    for (JDocMethod method : methods) {
                        sb.append("\n").append(method.isDeprecated() ? "~~" : "").append(method.getModifier() != null ? method.getModifier() + " " : "")
                                .append(method.getReturnType().getName()).append("\t[").append(method.getFullName())
                                .append("](").append(method.getURL()).append(")").append(method.isDeprecated() ? "~~" : "");
                    }
                    if (sb.length() > 0) {
                        String value = sb.toString();
                        if (sb.length() > 1024){
                            value = value.substring(1, 1024);
                            value = value.substring(0, value.lastIndexOf('\n') + 1) + "...";
                        }
                        eb.addField("Methods", value, false);
                    }
                    break;
                case "constructor.summary":
                    ArrayList<JDocConstructor> constructors = parseConstructors(summEl);
                    sb = new StringBuilder();
                    for (JDocConstructor constructor : constructors){
                        sb.append("\n").append(constructor.isDeprecated() ? "~~" : "").append(constructor.getFullName()).append(constructor.isDeprecated() ? "~~" : "");
                    }
                    if (sb.length() > 0) {
                        String value = sb.toString();
                        if (sb.length() > 1024){
                            value = value.substring(1, 1024);
                            value = value.substring(0, value.lastIndexOf('\n') + 1) + "...";
                        }
                        eb.addField("Constructors", value, false);
                    }
            }

        }
        Elements titles = doc.getElementsByClass("title");
        StringBuilder sb = new StringBuilder();
        for (Element title : titles){
            sb.append(title.text()).append(": ").append(title.attr("title")).append("\n");
        }
        eb.addField("Titles", sb.toString(), false);
        eb.setFooter("This is a WIP, errors could occur");
        return eb;

    }

    public ArrayList<JDocMethod> parseMethods(Element element) {
        ArrayList<JDocMethod> list = new ArrayList<>();


        Elements rows = element.getElementsByTag("tr");
        for (Element row : rows) {
            if (row.hasAttr("id")) {
                Element method = row.getElementsByClass("colSecond").first();
                JDocMethod jDMethod = new JDocMethod(method.text().substring(0, method.text().indexOf('(')), translateLink(method.getElementsByTag("a").first().attr("href")));
                Element modtype = row.getElementsByClass("colFirst").first();
                Elements ref = modtype.getElementsByTag("a");
                if (ref.size() > 0) {
                    jDMethod.setReturnType(new JDocLink(ref.first().text(), translateLink(ref.first().attr("href"))));
                    Element codeChild = modtype.child(0);
                    if (codeChild.html().indexOf('<') > 0) {
                        jDMethod.setModifier(codeChild.html().substring(0, codeChild.html().indexOf('<')).trim());
                    }
                } else {
                    jDMethod.setReturnType(new JDocLink(modtype.text()));
                }
                jDMethod.setDescription(row.getElementsByClass("colLast").first().text());
                if (row.getElementsByClass("deprecatedLabel").size() > 0) {
                    jDMethod.setDeprecated(true);
                }
                Elements hrefs = method.getElementsByTag("a");
                int j = 1;
                String args = method.text().substring(method.text().indexOf('(') + 1, method.text().length() - 1);
                if (args.length() > 0) {
                    String[] arguments = args.split(",");
                    for (int i = 0; i < arguments.length; i++) {
                        String[] split = arguments[i].split(" ");
                        if (j < hrefs.size() && hrefs.get(j).text().equals(split[0])) {
                            jDMethod.addArgument(split[1], new JDocLink(split[0], translateLink(hrefs.get(j).attr("href"))));
                            j++;
                        } else {
                            jDMethod.addArgument(split[1], new JDocLink(split[0]));
                        }
                    }
                }


                list.add(jDMethod);
            }
        }


        return list;
    }

    public ArrayList<JDocConstructor> parseConstructors(Element element) {
        ArrayList<JDocConstructor> list = new ArrayList<>();

        Elements rows = element.getElementsByTag("tr");
        for (Element row : rows) {
            if (row.hasClass("altColor") || row.hasClass("rowColor")){
                Element constructor = row.getElementsByClass("colConstructorName").first();
                JDocConstructor jDConstructor = new JDocConstructor(constructor.text().substring(0, constructor.text().indexOf('(')), translateLink(constructor.getElementsByTag("a").first().attr("href")));

                if (row.getElementsByClass("deprecatedLabel").size() > 0) {
                    jDConstructor.setDeprecated(true);
                }
                Elements hrefs = constructor.getElementsByTag("a");
                int j = 1;
                String args = constructor.text().substring(constructor.text().indexOf('(') + 1, constructor.text().length() - 1);
                if (args.length() > 0) {
                    String[] arguments = args.split(",");
                    for (int i = 0; i < arguments.length; i++) {
                        String[] split = arguments[i].split(" ");
                        if (j < hrefs.size() && hrefs.get(j).text().equals(split[0])) {
                            jDConstructor.addArgument(split[1], new JDocLink(split[0], translateLink(hrefs.get(j).attr("href"))));
                            j++;
                        } else {
                            jDConstructor.addArgument(split[1], new JDocLink(split[0]));
                        }
                    }
                }
                list.add(jDConstructor);
            }
        }


        return list;
    }
}
