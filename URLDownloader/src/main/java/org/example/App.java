package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.awt.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class App  {

    private static String LINK = "https://ru.hexlet.io/qna/java/questions/kak-preobrazovat-massiv-char-v-string-java";

    public static void main(String[] args) throws IOException, URISyntaxException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        ConvertLink();
        Document document = Jsoup.connect(LINK).get();
        PageParser parser = new PageParser(new URI(LINK));
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
}
