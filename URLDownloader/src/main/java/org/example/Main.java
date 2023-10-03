package org.example;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Main {

    private static String LINK = "https://freecodecamp.org/news/how-to-make-a-landing-page-with-html-css-and-javascript/";
    private static String SAVE_PATH = "./src/";
    private static String WEBPAGE_TYPE = ".html";
    private static String[] WEBPAGE_ACCESS = {".html", ".com", ".ru", ".org"};

    public static void main(String[] args) throws IOException {

        ConvertLink();
        WEBPAGE_TYPE = GetWebPageType(LINK);
        PageDownloader("page");
        OpenMainHTML();


    }

    /**
     * The CheckDomain() function can verify if a valid <br>
     * domain is being used. Currently, the following formats <br>
     * are supported:
     * <ul>
     * <li> html
     * <li> com
     * <li> ru
     * <li> org
     * </ul>
     * However, it is always easy to add a domain name if needed <br>
     * for parsing a specific website.
     *<br><br>
     * return meaning:
     * <ul>
     * <li> true, if so domain exists </li>
     * <li> false, if so domain doesn't exists</li>
     * </ul>
     */
    private static boolean CheckDomain() {

        for(int DomenIndex = 0; DomenIndex < WEBPAGE_ACCESS.length; DomenIndex++)
            if(WEBPAGE_ACCESS[DomenIndex].equals(WEBPAGE_TYPE))
                return true;
        return false;
    }

    /**
     * The CheckDomain() function reduces the link to a <br>
     * state without parameters.The part after the '?' sign <br>
     * is cut off. <br><br>
     * return meaning: void
     */
    private static void ConvertLink() {

        byte[] ByteLink = LINK.getBytes();
        LINK = "";
        for(int ByteLinkIndex = 0; ByteLinkIndex < ByteLink.length; ByteLinkIndex++) {

            if(ByteLink[ByteLinkIndex] == (byte)'?')
                break;
            LINK += (char)ByteLink[ByteLinkIndex];
        }
    }

    /**
     * The GetWebPageType(String) gets domain address.
     * <br>
     * Params:
     * <ul>
     * <li>Link - resource link</li>
     * </ul>
     * return meaning: type of web page
     */
    public static String GetWebPageType(String Link) {

        String WebPageType = Link.substring(Link.lastIndexOf('.'));
        if(WebPageType.indexOf('/') != -1)
            return ".html";

        return Link.substring(Link.lastIndexOf('.'));
    }

    /**
     * The OpenMainHTML() opens the main.html automatically, <br>
     * that contains all information about web page. <br><br>
     * return meaning: void
     */
    private static void OpenMainHTML() throws IOException {

        Desktop desktop = Desktop.getDesktop();
        desktop.open(new File(SAVE_PATH + "webparse/main.html"));
    }

    /**
     * The PageDownloader(String) downloads file from your LINK. <br>
     * There are two possible options:
     * <ul>
     * <li> 1) An object PageParser will be created, which works with <br>
     * a web page. </li>
     * <li> 2) A function will be called that loads a non-web file. </li>
     * </ul>
     * Params:
     * <ul>
     * <li>Link - resource link</li>
     * </ul>
     * return meaning: void
     */
    private static void PageDownloader(String Link) throws IOException {


        if(CheckDomain()) {

            try {
                PageParser parser = new PageParser(LINK, SAVE_PATH);
            } catch (Exception exception) {
                System.out.println(exception.getMessage() + "\nIN FILE: Main.java; IN STR: 68");
            }
        }

        else
            SimplePageDownloader(Link);
    }

    /**
     * The PageDownloader(String) function parses data from the link to <br>
     * the webparse folder. This function is needed for files that are not <br>
     * web pages. <br><br>
     * Params:
     * <ul>
     * <li>FileName - name of file</li>
     * </ul>
     * return meaning: void
     */
    private static void SimplePageDownloader(String FileName) throws IOException {

        URL url = new URL(LINK);
        URLConnection connection = url.openConnection();
        InputStream stream = url.openStream();
        Files.copy(stream, Paths.get("webparse/" + FileName + WEBPAGE_TYPE));
    }
}
