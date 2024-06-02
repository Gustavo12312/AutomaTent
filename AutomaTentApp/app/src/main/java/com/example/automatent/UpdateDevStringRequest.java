package com.example.automatent;

public class UpdateDevStringRequest {
    private String value;
    public UpdateDevStringRequest(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return "UpdateDataRequest{" +
                "value=" + value +
                '}';
    }
}
