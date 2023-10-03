package org.example;

import org.jsoup.Jsoup;
import org.jsoup.internal.NonnullByDefault;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PageParser {

    private String link;
    private static Document document;
    private StringBuilder HTMLBuilder;
    private List<LinkFile> Resources;

    public PageParser() {
        this.Resources = new ArrayList<>();
    }
    public PageParser(String link) throws IOException {

        this.link = link;
        this.document = Jsoup.connect(link).get();
        this.HTMLBuilder = new StringBuilder(document.html());
        this.Resources = new ArrayList<>();

        ParseBlock("img");
        ParseBlock("script");
        ParseBlock("link");

        for(int IndexResource = 0; IndexResource < Resources.size(); IndexResource++) {

            try {
                save(Resources.get(IndexResource));
            } catch (Exception e) {
                System.out.println(e.getMessage() + "\nIN FILE: PageParser.java; IN STR: 45");
            }
        }

        document = Jsoup.connect(link).get();
        ReplacePath("img", "src");
        ReplacePath("link", "href");
        ReplacePath("script", "src");

        try {

            String HTMLBuffer = document.html();
            new File("./webparse").mkdirs();
            FileWriter writer = new FileWriter("./webparse/main.html", true);
            writer.write(HTMLBuffer);
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage() + " \nIN FILE: PageParser.java; IN STR: 68");
        }
    }

    private void ParseBlock(String ParseTag) {

        for(Element element : document.getElementsByTag(ParseTag)) {

            switch (ParseTag) {

                case "img", "script":
                    ParseFile(element, "src");
                    break;

                case "link":
                    ParseFile(element, "href");
                    break;

                default:
                    break;
            }
        }
    }

    private void ParseFile(Element element, String AttrName) {

        String LinkPath = element.attr(AttrName);
        int LinkPathLastIndex = LinkPath.lastIndexOf('/');
        if(LinkPath.length() != 0) {

            if(LinkPath.substring(0, 5).equals("https") != true)
                Resources.add(new LinkFile(element.absUrl(AttrName), LinkPath.substring(0, LinkPathLastIndex),
                        LinkPath.substring(LinkPathLastIndex)));
        }
    }

    private void ReplacePath(String TagName, String SelectName) {

        for(Element TagElement : document.select(TagName + "[" + SelectName + "]")) {

            String tag = TagElement.attr(SelectName);

            if(tag.substring(0, 2).equals("//"))
                TagElement.attr(SelectName, "." + TagElement.attr(SelectName).substring(1));

            else if(tag.substring(0, tag.indexOf('/')).equals("https:") == false) {

                if(tag.charAt(0) != '/') {

                    TagElement.attr(SelectName, "./" + TagElement.attr(SelectName));
                    continue;
                }

                TagElement.attr(SelectName, "." + TagElement.attr(SelectName));
            }
        }
    }

    private void save(LinkFile linkFile) throws IOException {

        new File("./webparse" + linkFile.getPath()).mkdirs();
        URL url = new URL(linkFile.getLink());
        URLConnection connection = url.openConnection();
        InputStream stream = url.openStream();
        Files.copy(stream, Paths.get("./webparse" + linkFile.getPath() + linkFile.getName()));
    }
}
