package com.sourceartists.auction.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.List;

public class Auction {

    @Id
    @GeneratedValue
    private Integer id;

    private String title;
    private String description;

    private BigDecimal initialBidMinimumValue;

    @OneToMany(mappedBy = "auction")
    private List<Bid> bids;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getInitialBidMinimumValue() {
        return initialBidMinimumValue;
    }

    public void setInitialBidMinimumValue(BigDecimal initialBidMinimumValue) {
        this.initialBidMinimumValue = initialBidMinimumValue;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }
}
