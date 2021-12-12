package by.bntu.fitr.springtry.data;

import org.springframework.web.multipart.MultipartFile;
import java.util.Arrays;
import java.sql.Timestamp;
import java.util.List;

public class LotData {
    private MultipartFile imagePath1;
    private MultipartFile imagePath2;
    private MultipartFile imagePath3;
    private MultipartFile imagePath4;
    private MultipartFile imagePath5;
    private String name;
    private String description;
    private String startBid;
    private String startTime;
    private String finishTime;

    public List<MultipartFile> getImages() {
        return Arrays.asList(imagePath1, imagePath2, imagePath3, imagePath4, imagePath5);
    }

    public MultipartFile getImagePath1() {
        return imagePath1;
    }

    public void setImagePath1(MultipartFile imagePath1) {
        this.imagePath1 = imagePath1;
    }

    public MultipartFile getImagePath2() {
        return imagePath2;
    }

    public void setImagePath2(MultipartFile imagePath2) {
        this.imagePath2 = imagePath2;
    }

    public MultipartFile getImagePath3() {
        return imagePath3;
    }

    public void setImagePath3(MultipartFile imagePath3) {
        this.imagePath3 = imagePath3;
    }

    public MultipartFile getImagePath4() {
        return imagePath4;
    }

    public void setImagePath4(MultipartFile imagePath4) {
        this.imagePath4 = imagePath4;
    }

    public MultipartFile getImagePath5() {
        return imagePath5;
    }

    public void setImagePath5(MultipartFile imagePath5) {
        this.imagePath5 = imagePath5;
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }
}
