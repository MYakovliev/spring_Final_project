package by.bntu.fitr.springtry.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * Entity class Lot
 *
 * @author Nikita Yakovlev
 */
@Entity
@Table(name = "lots")
public class Lot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idlots")
    private long id;
    private String name;
    private String description;
    @Column(name = "start_time")
    private Timestamp startTime;
    @Column(name = "end_time")
    private Timestamp finishTime;
    @Column(name = "bid")
    private BigDecimal currentCost;
    @ManyToOne
    @JoinColumn(name = "seller")
    private User seller;
    @Transient
    private List<Bid> bidHistory;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "lot_id")
    private List<LotImage> images;

    public Lot() {
    }

    public Lot(long id, String name, String description, Timestamp startTime, Timestamp finishTime, BigDecimal currentCost, User seller, List<String> images) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.currentCost = currentCost;
        this.seller = seller;
        setImages(images);
        this.bidHistory = new ArrayList<>();
    }

    public List<Bid> getBidHistory() {
        return bidHistory;
    }

    public void setBidHistory(List<Bid> bidHistory) {
        this.bidHistory = new ArrayList<>(bidHistory);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Timestamp finishTime) {
        this.finishTime = finishTime;
    }

    public BigDecimal getCurrentCost() {
        Optional<Bid> any = bidHistory.stream().filter(bid -> bid.getStatus() == Status.WINING).findAny();
        if (any.isPresent()){
            return any.get().getBid();
        }
        return currentCost;
    }

    public void setCurrentCost(BigDecimal currentCost) {
        this.currentCost = currentCost;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User sellerId) {
        this.seller = sellerId;
    }

    public User getBuyer() {
        User buyer = null;
        Optional<Bid> first = bidHistory.stream().sorted(Comparator.comparing(Bid::getBid)).findFirst();
        if (first.isPresent()){
            buyer = first.get().getBuyer();
        }
        return buyer;
    }

    public List<String> getImages() {
        List<String> imageList = new ArrayList<>();
        images.forEach(lotImage -> imageList.add(lotImage.getImage()));
        return imageList;
    }

    public void setImages(List<String> images) {
        List<LotImage> lotImages = new ArrayList<>();
        images.forEach(image -> lotImages.add(new LotImage(0, image)));
        this.images = lotImages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lot lot = (Lot) o;

        if (id != lot.id) return false;
        if (name != null ? !name.equals(lot.name) : lot.name != null) return false;
        if (description != null ? !description.equals(lot.description) : lot.description != null) return false;
        if (startTime != null ? !startTime.equals(lot.startTime) : lot.startTime != null) return false;
        if (finishTime != null ? !finishTime.equals(lot.finishTime) : lot.finishTime != null) return false;
        if (currentCost != null ? !currentCost.equals(lot.currentCost) : lot.currentCost != null) return false;
        if (seller != null ? !seller.equals(lot.seller) : lot.seller != null) return false;
        if (bidHistory != null ? !bidHistory.equals(lot.bidHistory) : lot.bidHistory != null) return false;
        return images != null ? images.equals(lot.images) : lot.images == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (finishTime != null ? finishTime.hashCode() : 0);
        result = 31 * result + (currentCost != null ? currentCost.hashCode() : 0);
        result = 31 * result + (seller != null ? seller.hashCode() : 0);
        result = 31 * result + (bidHistory != null ? bidHistory.hashCode() : 0);
        result = 31 * result + (images != null ? images.hashCode() : 0);
        return result;
    }

    @Override
    public String
    toString() {
        final StringBuilder sb = new StringBuilder("Lot{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", startTime=").append(startTime);
        sb.append(", finishTime=").append(finishTime);
        sb.append(", currentCost=").append(currentCost);
        sb.append(", seller=").append(seller);
        sb.append(", bidHistory=").append(bidHistory);
        sb.append(", images=").append(images);
        sb.append('}');
        return sb.toString();
    }
}
