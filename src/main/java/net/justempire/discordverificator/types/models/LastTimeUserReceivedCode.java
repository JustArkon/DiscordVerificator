package net.justempire.discordverificator.types.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class LastTimeUserReceivedCode {
    @JsonProperty("ip")
    private String ip;

    @JsonProperty("sent")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timeOfReceiving;

    // Empty constructor for Jackson to serialize/deserialize data from/to JSON
    public LastTimeUserReceivedCode() { }

    public LastTimeUserReceivedCode(String ip, Date timeOfReceiving) {
        this.ip = ip;
        this.timeOfReceiving = timeOfReceiving;
    }

    public String getIp() {
        return ip;
    }

    public Date getTimeOfReceiving() {
        return timeOfReceiving;
    }

    public void setTimeOfReceiving(Date sent) {
        this.timeOfReceiving = sent;
    }
}
