package com.sourceartists.auction.service;

import com.sourceartists.auction.exception.WatchingException;
import com.sourceartists.auction.model.Bid;
import com.sourceartists.auction.model.Rating;

import java.math.BigDecimal;

public interface AuctionService {

    boolean bidOnAuction(Integer auctionId, Bid bid);

    void rateSeller(Integer sellerId, Rating newRating);

    boolean watchAnOffer(Integer auctionId, Integer personId) throws WatchingException;
}
