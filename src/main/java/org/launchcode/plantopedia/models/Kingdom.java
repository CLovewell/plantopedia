package org.launchcode.plantopedia.models;

public class Kingdom extends Taxon {
    private String name;
    private Links links;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }
}
