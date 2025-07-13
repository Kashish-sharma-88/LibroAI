package com.example.libroai;

import java.util.HashMap;
import java.util.Map;

public class RequestStatus {
    private String status;

    public RequestStatus() {
    }

    public RequestStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        return map;
    }
}
