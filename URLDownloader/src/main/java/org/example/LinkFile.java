package org.example;

public class LinkFile {

    private String link;
    private String path;
    private String name;

    public LinkFile(String link, String path, String name) {

        this.link = link;
        this.path = path;
        this.name = name;

        if(this.path.equals(this.name)) {
            this.path = "/";
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
