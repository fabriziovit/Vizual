package com.app.vizual.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class IntegerModel {
    @SerializedName("data")
    private ArrayList<Integer> data;

    public IntegerModel() {}

    public IntegerModel(ArrayList<Integer> data) {
        this.data = data;
    }

    public ArrayList<Integer> getData() { return data; }

    public void setData(ArrayList<Integer> data) { this.data = data; }
}
