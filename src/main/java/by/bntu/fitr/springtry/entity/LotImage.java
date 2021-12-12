package by.bntu.fitr.springtry.entity;

import javax.persistence.*;

@Table(name = "lot_images")
@Entity
public class LotImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idlot_images")
    private long id;
    private String image;
    @Column(name="lot_id")
    private long lotId;

    public LotImage(long id, String image, long lotId) {
        this.id = id;
        this.image = image;
        this.lotId = lotId;
    }

    public LotImage() {
    }

    public long getLotId() {
        return lotId;
    }

    public void setLotId(long lotId) {
        this.lotId = lotId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LotImage lotImage = (LotImage) o;

        if (id != lotImage.id) return false;
        return image != null ? image.equals(lotImage.image) : lotImage.image == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (image != null ? image.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LotImage{");
        sb.append("id=").append(id);
        sb.append(", image='").append(image).append('\'');
        sb.append('}');
        return sb.toString();
    }
}