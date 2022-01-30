package com.app.vizual.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ListImages {
    @SerializedName("data")
    private ArrayList<String> data;

    public ListImages() {}

    public ListImages(ArrayList<String> data) { this.data = data; }

    public ArrayList<String> getData() { return data; }

    public void setData(ArrayList<String> data) { this.data = data; }
}
