package com.app.vizual.Models;

public class Image {
    private String name;
    private int left;
    private int top;
    private int width;
    private int height;
    private String extension;

    public Image() {
    }

    public Image(String name, int left, int top, int width, int height) {
        this.name = name;
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;
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

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
