package by.bntu.fitr.springtry.data;

import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.List;

public class LotData {
    private List<MultipartFile> pictures;
    private String name;
    private String description;
    private String startBid;
    private Timestamp startTime;
    private Timestamp finishTime;

    public List<MultipartFile> getPictures() {
        return pictures;
    }

    public void setPictures(List<MultipartFile> pictures) {
        this.pictures = pictures;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartBid() {
        return startBid;
    }

    public void setStartBid(String startBid) {
        this.startBid = startBid;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Timestamp finishTime) {
        this.finishTime = finishTime;
    }
}
