package org.example;

import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.Scanner;

public class Main {

    private static String LINK = "https://mipt.ru";
    private static String SAVE_PATH = "./src";
    private static String WEBPAGE_TYPE = ".html";
    private static String[] WEBPAGE_ACCESS = {".html", ".com", ".ru", ".org", ".xhtml"};

    public static void main(String[] args) throws IOException, URISyntaxException {

        Initialize(args);
        LINK = ConvertLink(LINK);
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
    public static String ConvertLink(String Link) {

        byte[] ByteLink = Link.getBytes();
        Link = "";
        for(int ByteLinkIndex = 0; ByteLinkIndex < ByteLink.length;
                                                   ByteLinkIndex++) {
            if(ByteLink[ByteLinkIndex] == (byte)'?')
                break;
            Link += (char)ByteLink[ByteLinkIndex];
        }
        return Link;
    }

    /**
     * The DeleteDirectory() deletes the directory with path, that you printed<br>
     * in command line. If you press 'y' and terminates the program if you<br>
     * do not press 'y'. <br>
     * return meaning: void
     */
    private static void DeleteDirectory() throws IOException {

        Scanner AnswerScanner = new Scanner(System.in);
        String answer = AnswerScanner.nextLine();
        if(!(answer.toLowerCase().equals("y") ||
                answer.toLowerCase().equals("yes")))
            System.exit(0);

        FileUtils.deleteDirectory(new File(SAVE_PATH + "/webparse"));
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
    private static String GetWebPageType(String Link) {

        String WebPageType = Link.substring(Link.lastIndexOf('.'));
        if(WebPageType.indexOf('/') != -1)
            return ".html";
        return Link.substring(Link.lastIndexOf('.'));
    }

    /**
     * The Initialize(String[]) initializes LINK and SAVE_PATH from the command line.<br>
     * You enter the values for the link and save path yourself. If a folder already<br>
     * exists at this path, you will be prompted to replace it.<br>
     * Params:
     * <ul>
     * <li>args - args from command line</li>
     * </ul>
     * return meaning: void
     */
    private static void Initialize(String[] args) throws IOException {

        try {
            LINK = args[0];
            SAVE_PATH = args[1] + "/";
        } catch (Exception exception) {
            TRACE("ARGS IS NULL", "Main.java",
                   new Throwable().getStackTrace()[0].getLineNumber());
            System.exit(1);
        }

        if((new File(SAVE_PATH + "/webparse")).exists()) {

            System.out.println("So directory already exists");
            System.out.println("Do you want to change directory ?(y/n)");
            DeleteDirectory();
        }
    }

    /**
     * The OpenMainHTML() opens the main.html automatically, <br>
     * that contains all information about web page. <br><br>
     * return meaning: void
     */
    private static void OpenMainHTML() throws IOException {

        Desktop desktop = Desktop.getDesktop();
        desktop.open(new File(SAVE_PATH + "/webparse/main.html"));
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
                TRACE(exception.getMessage(), "Main.java",
                        new Throwable().getStackTrace()[0].getLineNumber());
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
    private static void SimplePageDownloader(String FileName)
                                             throws IOException {
        URL url = new URL(LINK);
        URLConnection connection = url.openConnection();
        InputStream stream = url.openStream();
        Files.copy(stream, Paths.get("webparse/" + FileName + WEBPAGE_TYPE));
    }

    /**
     * The TRACE(String, String, int) displays warnings and errors.<br><br>
     * Params:
     * <ul>
     * <li>Message - message of warning or error</li>
     * <li>Class - name of class</li>
     * <li>Line - number of line</li>
     * </ul>
     * return meaning: void
     */
    public static void TRACE(String Message, String Class,
                                                 int Line)  {
        System.out.println();
        System.out.println("###############################");
        System.out.println("-------------TRACE-------------");
        System.out.println("Exception/Warning: " +   Message);
        System.out.println("Class: "             +     Class);
        System.out.println("Line: "              +      Line);
        System.out.println("###############################");
    }
}
