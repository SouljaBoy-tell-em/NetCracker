package org.example;

public class LinkFile {

    private String link;
    private String path;
    private String name;

    public LinkFile(String link, String path, String name) {

        this.link = link;
        this.path = path;
        this.name = name;

        System.out.println(link);

        if(this.path.length() > 0) {
            if(this.path.charAt(0) != '/')
                this.path = "/" + this.path;

            if(this.name.charAt(0) != '/')
                this.name = "/" + this.name;
        }
    }

    public String getLink() {
        return link;
    }
    public String getPath() {
        return path;
    }
    public String getName() {return name;}
}
