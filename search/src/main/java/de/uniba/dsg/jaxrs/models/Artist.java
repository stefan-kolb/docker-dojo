package de.uniba.dsg.jaxrs.models;

public class Artist {
    private String id;
    private String name;

    public Artist() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
