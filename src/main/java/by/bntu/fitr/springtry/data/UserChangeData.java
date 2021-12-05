package by.bntu.fitr.springtry.data;

import org.springframework.web.multipart.MultipartFile;

public class UserChangeData {
    private MultipartFile avatar;
    private String mail;
    private String name;

    public MultipartFile getAvatar() {
        return avatar;
    }

    public void setAvatar(MultipartFile avatar) {
        this.avatar = avatar;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
