package org.example;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    private static String LINK = "https://blog.hubspot.com/marketing/web-design-html-css-javascript";
    private static String WEBPAGE_TYPE = ".html";
    private static String[] WEBPAGE_ACCESS = {".html", ".com", ".ru", ".org"};

    public static void main(String[] args) throws IOException {

        ConvertLink();
        WEBPAGE_TYPE = GetWebPageType(LINK);
        PageDownloader("page");
        OpenMainHTML();
    }

    private static boolean CheckDomain() {

        for(int DomenIndex = 0; DomenIndex < WEBPAGE_ACCESS.length; DomenIndex++)
            if(WEBPAGE_ACCESS[DomenIndex].equals(WEBPAGE_TYPE))
                return true;
        return false;
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

    private static void OpenMainHTML() throws IOException {

        Desktop desktop = Desktop.getDesktop();
        desktop.open(new File("./webparse/main.html"));
    }

    private static void PageDownloader(String FileName) throws IOException {


        if(CheckDomain()) {

            try {
                PageParser parser = new PageParser(LINK);
            } catch (Exception exception) {
                System.out.println(exception.getMessage() + "\nIN FILE: Main.java; IN STR: 68");
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
