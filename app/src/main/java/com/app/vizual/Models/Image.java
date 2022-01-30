package com.app.vizual.Models;

public class Image {
    private String name;
    private String extension;

    public Image() {
    }

    public Image(String name, String extension) {
        this.name = name;
        this.extension = extension;
    }

    public Image(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
