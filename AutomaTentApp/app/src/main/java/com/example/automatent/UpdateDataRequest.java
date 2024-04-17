package com.example.automatent;

public class UpdateDataRequest {
    private Integer value;
    public UpdateDataRequest(Integer value) {
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
