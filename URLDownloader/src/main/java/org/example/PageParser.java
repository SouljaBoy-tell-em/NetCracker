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
                Save(Resources.get(IndexResource));
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

    /**
     * The ParseBlock(String) function works depending on <br>
     * the ParseTag tag and passes the attribute of this tag <br>
     * to the ParseFile(String, Element) function for subsequent <br>
     * downloading of the file from the link. <br><br>
     * Params:
     * <ul>
     * <li>ParseTag - tag of html file</li>
     * </ul>
     * return meaning: void
     */
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

    /**
     * The ParseFile(Element, String) adds the link to the file in the resources<br>
     * list, which will be parsed later.<br><br>
     * Params:
     * <ul>
     * <li>element - element from the ParseBlock(String)</li>
     * <li>AttrName - name of attribute</li>
     * </ul>
     * return meaning: void
     */
    private void ParseFile(Element element, String AttrName) {

        String LinkPath = element.attr(AttrName);
        int LinkPathLastIndex = LinkPath.lastIndexOf('/');
        if(LinkPath.length() != 0) {

            if(LinkPath.substring(0, 5).equals("https") != true)
                Resources.add(new LinkFile(element.absUrl(AttrName), LinkPath.substring(0, LinkPathLastIndex),
                        LinkPath.substring(LinkPathLastIndex)));
        }
    }

    /**
     * The ReplacePath(String, String) replaces the path in the HTML file with <br>
     * a resource that has been downloaded to a local folder with <br>
     * a folder structure. <br><br>
     * Params:
     * <ul>
     * <li>TagName - tag of html file</li>
     * <li>AttrName - name of attribute</li>
     * </ul>
     * return meaning: void
     */
    private void ReplacePath(String TagName, String AttrName) {

        for(Element TagElement : document.select(TagName + "[" + AttrName + "]")) {

            String tag = TagElement.attr(AttrName);

            if(tag.substring(0, 2).equals("//"))
                TagElement.attr(AttrName, "." + TagElement.attr(AttrName).substring(1));

            else if(tag.substring(0, tag.indexOf('/')).equals("https:") == false) {

                if(tag.charAt(0) != '/') {

                    TagElement.attr(AttrName, "./" + TagElement.attr(AttrName));
                    continue;
                }

                TagElement.attr(AttrName, "." + TagElement.attr(AttrName));
            }
        }
    }

    /**
     * The Save(LinkFile) saves a file from a link and places <br>
     * it into a specific folder structure. <br><br>
     * Params:
     * <ul>
     * <li>linkFile - object, that contains information <br>
     * about parsing file</li>
     * </ul>
     * return meaning: void
     */
    private void Save(LinkFile linkFile) throws IOException {

        new File("./webparse" + linkFile.getPath()).mkdirs();
        URL url = new URL(linkFile.getLink());
        URLConnection connection = url.openConnection();
        InputStream stream = url.openStream();
        Files.copy(stream, Paths.get("./webparse" + linkFile.getPath() + linkFile.getName()));
    }
}
