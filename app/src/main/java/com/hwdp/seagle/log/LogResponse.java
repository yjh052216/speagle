package com.hwdp.seagle.log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LogResponse {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("details")
    @Expose
    private String details;

    @Override
    public String toString() {
        return "{" +
                "status='" + status + '\'' +
                ", details='" + details + '\'' +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
