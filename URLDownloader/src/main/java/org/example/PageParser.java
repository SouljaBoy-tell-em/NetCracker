package org.example;

import org.jsoup.Jsoup;
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
    private URI link;
    private static Document document;
    private StringBuilder HTMLBuilder;
    private List<LinkFile> Resources;
    public PageParser() {

        this.Resources = new ArrayList<>();
    }
    public PageParser(URI link) throws IOException, NoSuchMethodException, InvocationTargetException,
                                       IllegalAccessException {

        this.link = link;
        this.document = Jsoup.connect(String.valueOf(link)).get();
        this.HTMLBuilder = new StringBuilder(document.html());
        this.Resources = new ArrayList<>();

        ParseBlock("img");
        ParseBlock("script");
        ParseBlock("link");

        for(int i = 0; i < Resources.size(); i++) {

            try {
                save(Resources.get(i));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        document = Jsoup.connect("https://htmlbook.ru/samhtml/struktura-html-koda").get();
        for(Element img : document.select("img[src]"))
            img.attr("src", "." + img.attr("src"));

        for(Element css : document.select("link[href]"))
            css.attr("href", "." + css.attr("href"));

        for(Element css : document.select("script[src]"))
            css.attr("src", "." + css.attr("src"));

        String s = document.html();

        try {
            FileWriter writer = new FileWriter("./webparse/main.html", true);

            writer.write(s);
            writer.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void ParseBlock(String ParseTag)
           throws InvocationTargetException, IllegalAccessException {

        for(Element element : document.getElementsByTag(ParseTag)) {

            switch (ParseTag) {

                case "img":
                    String file = element.attr("src");
                    if(file.length() != 0 && file.substring(0, file.lastIndexOf('/')).indexOf('.') == -1 && file.substring(file.lastIndexOf('/')).indexOf('.') != -1)
                        Resources.add(new LinkFile(element.absUrl("src"), file.substring(0, file.lastIndexOf('/')), file.substring(file.lastIndexOf('/'))));
                    break;

                case "link":
                    String LinkPath = element.attr("href");
                    if(LinkPath.length() != 0 && LinkPath.substring(0, LinkPath.lastIndexOf('/')).indexOf('.') == -1 && LinkPath.substring(LinkPath.lastIndexOf('/')).indexOf('.') != -1)
                        Resources.add(new LinkFile(element.absUrl("href"), LinkPath.substring(0, LinkPath.lastIndexOf('/')), LinkPath.substring(LinkPath.lastIndexOf('/'))));
                    break;

                case "script":
                    String file1 = element.attr("src");
                    if(file1.length() != 0 && file1.substring(0, file1.lastIndexOf('/')).indexOf('.') == -1 && file1.substring(file1.lastIndexOf('/')).indexOf('.') != -1) {
                        Resources.add(new LinkFile(element.absUrl("src"), file1.substring(0, file1.lastIndexOf('/')), file1.substring(file1.lastIndexOf('/'))));
                    }
                    break;

                default:
                    break;
            }
        }
    }

    private void save(LinkFile linkFile) throws IOException {

//        new File("./webparse" + linkFile.getPath()).mkdirs();
//        URL url = new URL(linkFile.getLink());
//        URLConnection connection = url.openConnection();
//        InputStream stream = url.openStream();
//        Files.copy(stream, Paths.get("./webparse" + linkFile.getPath() + linkFile.getName()));


        new File("./webparse" + linkFile.getPath()).mkdirs();
        URL url = new URL(linkFile.getLink());
        URLConnection connection = url.openConnection();
        InputStream in = url.openStream();
        BufferedInputStream bis = new BufferedInputStream(in);
        FileOutputStream fos = new FileOutputStream("./webparse" + linkFile.getPath() + linkFile.getName());

            byte[] data = new byte[1024];
            int count;
            while ((count = bis.read(data, 0, 1024)) != -1) {
                fos.write(data, 0, count);
            }
    }
}
