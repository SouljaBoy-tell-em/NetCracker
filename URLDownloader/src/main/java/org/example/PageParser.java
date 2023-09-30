package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PageParser {
    private URI link;
    private static Document document;
    private StringBuilder HTMLBuilder;
    private List<String> ImgLinks;
    private List<String> CSSLinks;
    private List<List<Node>> JSLinks;
    public PageParser() {

        this.ImgLinks = new ArrayList<>();
        this.CSSLinks = new ArrayList<>();
        this.JSLinks =  new ArrayList<>();
    }
    public PageParser(URI link) throws IOException, NoSuchMethodException, InvocationTargetException,
                                       IllegalAccessException {

        this.link = link;
        this.document = Jsoup.connect(String.valueOf(link)).get();
        this.HTMLBuilder = new StringBuilder(document.html());

        this.ImgLinks = new ArrayList<>();
        this.CSSLinks = new ArrayList<>();
        this.JSLinks =  new ArrayList<>();

        ParseBlock("img");
        ParseBlock("link");
        ParseBlock("script");
//
//        for(int i = 0; i < CSSLinks.size(); i++)
//            save(CSSLinks.get(i), "css" + (i + 1) + ".css");
        
//        for(int i = 0; i < ImgLinks.size(); i++)
//            save(ImgLinks.get(i), "img" + (i + 1) + ".svg");

    }

    public void ParseBlock(String ParseTag)
           throws InvocationTargetException, IllegalAccessException {

        for(Element element : document.getElementsByTag(ParseTag)) {

            switch (ParseTag) {

                case "img":
                    ImgLinks.add(element.absUrl("src"));
                    break;

                case "link":
                    ParseCSS(element);
                    break;

                case "script":
                    JSLinks.add(element.childNodes());
                    break;

                default:
                    break;
            }
        }
    }

    private void ParseCSS(Element element) {

        String href = element.absUrl("href");
        if(href.substring(href.length() - 3, href.length()).equals("css"))
            CSSLinks.add(href);
    }

    private void save(String link, String FileName) throws IOException {

        URL url = new URL(link);
        URLConnection connection = url.openConnection();
        InputStream stream = url.openStream();
        Files.copy(stream, Paths.get("webparse/" + FileName));
    }
}
