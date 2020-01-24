package de.uniba.dsg.jaxrs.models;

public class Cover {
    private String url;

    public Cover() {}

    public Cover(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
