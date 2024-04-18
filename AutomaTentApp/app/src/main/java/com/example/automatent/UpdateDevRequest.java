package com.example.automatent;

public class UpdateDevRequest {
    private Integer value;
    public UpdateDevRequest(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return "UpdateDataRequest{" +
                "value=" + value +
                '}';
    }
}
