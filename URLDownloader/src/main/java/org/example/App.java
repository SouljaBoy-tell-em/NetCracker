package org.example;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App  {

    private static String LINK = "https://xn----7sbbaqhlkm9ah9aiq.net/news-new/css-html";
    private static String WEBPAGE_TYPE = ".html";

    public static void main(String[] args) throws IOException {

        ConvertLink();
        WEBPAGE_TYPE = GetWebPageType(LINK);
        PageDownloader("page");
    }

    private static void ConvertLink() {

        byte[] ByteLink = LINK.getBytes();
        LINK = "";
        for(int ByteLinkIndex = 0; ByteLinkIndex < ByteLink.length; ByteLinkIndex++) {

            if(ByteLink[ByteLinkIndex] == (byte)'?')
                break;
            LINK += (char)ByteLink[ByteLinkIndex];
        }
    }

    public static String GetWebPageType(String FileName) {

        String WebPageType = FileName.substring(FileName.lastIndexOf('.'));
        if(WebPageType.indexOf('/') != -1)
            return ".html";

        return FileName.substring(FileName.lastIndexOf('.'));
    }

    private static void PageDownloader(String FileName) throws IOException {

        if(WEBPAGE_TYPE.equals(".html") || WEBPAGE_TYPE.equals(".org")) {

            try {
                PageParser parser = new PageParser(LINK);
            } catch (Exception exception) {
                System.out.println(exception.getMessage() + "\nIN FILE: APP; STR: 52");
            }
        }

        else
            SimplePageDownloader(FileName);
    }

    private static void SimplePageDownloader(String FileName) throws IOException {

        URL url = new URL(LINK);
        URLConnection connection = url.openConnection();
        InputStream stream = url.openStream();
        Files.copy(stream, Paths.get("webparse/" + FileName + WEBPAGE_TYPE));
    }
}
