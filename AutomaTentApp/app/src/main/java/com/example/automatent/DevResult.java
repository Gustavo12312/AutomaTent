package com.example.automatent;

import com.google.gson.annotations.SerializedName;

public class DevResult {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("value")
    private String value;

    @SerializedName("value_string")
    private String valueString;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getValueString() {
        return valueString;
    }
}

