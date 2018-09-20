package com.demo.photoalbum.model;

public class Photos_Vm {
 String size_code;
 String url;
 int width;
 int height;
 float quality;
 String mime;

    public Photos_Vm(String size_code, String url, int width, int height, float quality, String mime) {
        this.size_code = size_code;
        this.url = url;
        this.width = width;
        this.height = height;
        this.quality = quality;
        this.mime = mime;
    }

    public String getSize_code() {
        return size_code;
    }

    public void setSize_code(String size_code) {
        this.size_code = size_code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public float getQuality() {
        return quality;
    }

    public void setQuality(float quality) {
        this.quality = quality;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }
}
