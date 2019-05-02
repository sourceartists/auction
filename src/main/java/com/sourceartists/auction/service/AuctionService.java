package com.sourceartists.auction.service;

import com.sourceartists.auction.model.Bid;

public interface AuctionService {

    boolean bidOnAuction(Integer auctionId, Bid bid);
}
