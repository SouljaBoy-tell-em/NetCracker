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
    private String SAVE_PATH;
    private static Document document;
    private StringBuilder HTMLBuilder;
    private List<LinkFile> Resources;

    public PageParser() {
        this.Resources = new ArrayList<>();
    }

    public PageParser(String link, String SAVE_PATH) throws IOException {

        this.link = link;
        this.SAVE_PATH = SAVE_PATH;
        this.document = Jsoup.connect(link).get();
        this.HTMLBuilder = new StringBuilder(document.html());
        this.Resources = new ArrayList<>();

        ParseBlock("img");
        ParseBlock("script");
        ParseBlock("link");

        Distribution();
        CreateHTML();
    }

    /**
     * The CreateHTML() function creates an HTML file with resource tags,<br>
     * whose paths are replaced with local ones and are located in a folder<br>
     * structure.<br><br>
     * return meaning: void
     */
    private void CreateHTML() {

        try {

            String HTMLBuffer = document.html();
            new File(SAVE_PATH + "/webparse").mkdirs();
            FileWriter writer = new FileWriter(SAVE_PATH +
                              "/webparse/main.html", true);
            writer.write(HTMLBuffer);
            writer.close();
        } catch (Exception exception) {
            Main.TRACE(exception.getMessage(), "PageParser.java",
                    new Throwable().getStackTrace()[0].getLineNumber());
        }
    }

    /**
     * The Distribution() saves and distributes files between them folders.<br><br>
     * return meaning: void
     */
    private void Distribution() {

        for(int IndexResource = 0; IndexResource < Resources.size();
                                                    IndexResource++) {
            try {
                Save(Resources.get(IndexResource));
            } catch (Exception exception) {
                Main.TRACE(exception.getMessage(), "PageParser.java",
                        new Throwable().getStackTrace()[0].getLineNumber());
            }
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
     * list, which will be parsed later and update path for already local file.<br><br>
     * Params:
     * <ul>
     * <li>element - element from the ParseBlock(String)</li>
     * <li>AttrName - name of attribute</li>
     * </ul>
     * return meaning: void
     */
    private void ParseFile(Element element, String AttrName) {

        if(element.absUrl(AttrName).length() != 0) {

            try {

                String Link = element.absUrl(AttrName);
                Link = Main.ConvertLink(Link);
                String PFLink = Link.substring(Link.indexOf("//") + 2);
                int PFLinkSize = PFLink.length() - PFLink.substring(
                        PFLink.lastIndexOf('/') + 1).length() - 1;
                String LinkFilePath = PFLink.substring(0, PFLinkSize);
                String LinkFileName =
                        PFLink.substring(PFLink.lastIndexOf('/') + 1);

                if(LinkFileName.indexOf('.') != -1) {

                    Resources.add(new LinkFile(Link,
                            LinkFilePath,
                            LinkFileName));
                    element.attr(AttrName,
                            "./" + LinkFilePath + "/" + LinkFileName);
                }
            } catch (Exception exception) {
                Main.TRACE(exception.getMessage(), "PageParser.java",
                        new Throwable().getStackTrace()[0].getLineNumber());
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

        new File(SAVE_PATH + "/webparse/" + linkFile.getPath()).mkdirs();
        URL url = new URL(linkFile.getLink());
        URLConnection connection = url.openConnection();
        InputStream stream = url.openStream();
        Files.copy(stream, Paths.get(SAVE_PATH + "/webparse/" +
                     linkFile.getPath() + "/" + linkFile.getName()));
    }
}
