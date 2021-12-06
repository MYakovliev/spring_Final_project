package by.bntu.fitr.springtry.data;

import by.bntu.fitr.springtry.entity.Lot;

public class PayData {
    private String bid;
    private long lotId;

    public long getLotId() {
        return lotId;
    }

    public void setLotId(long lotId) {
        this.lotId = lotId;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }
}
