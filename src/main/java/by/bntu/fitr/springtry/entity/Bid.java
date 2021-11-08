package by.bntu.fitr.springtry.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "bid_history")
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idbid_history")
    private long id;
    @ManyToOne
    @JoinColumn(name = "id_buyer")
    private User buyer;
    private BigDecimal bid;
    @Enumerated(EnumType.STRING)
    private Status status;

    public Bid() {
    }

    public Bid(long id, User buyer, BigDecimal bid, Status status) {
        this.id = id;
        this.buyer = buyer;
        this.bid = bid;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public BigDecimal getBid() {
        return bid;
    }

    public void setBid(BigDecimal bid) {
        this.bid = bid;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bid bid1 = (Bid) o;

        if (id != bid1.id) return false;
        if (buyer != null ? !buyer.equals(bid1.buyer) : bid1.buyer != null) return false;
        if (bid != null ? !bid.equals(bid1.bid) : bid1.bid != null) return false;
        return status == bid1.status;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (buyer != null ? buyer.hashCode() : 0);
        result = 31 * result + (bid != null ? bid.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Bid{");
        sb.append("id=").append(id);
        sb.append(", buyer=").append(buyer);
        sb.append(", bid=").append(bid);
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
