package com.sourceartists.auction.service.impl;

import com.sourceartists.auction.exception.AuctionLockedException;
import com.sourceartists.auction.model.Bid;
import com.sourceartists.auction.service.LoggingService;
import com.sourceartists.auction.service.helper.BidSaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BasicAuctionService {

    @Autowired
    private BidSaver bidSaver;
    @Autowired
    private LoggingService loggingService;

    private static final Integer MAX_BID_ATTEMPTS = Integer.valueOf(5);

    public boolean bidOnAuction(Integer auctionId, Bid bid){
        Integer attemptCount = Integer.valueOf(0);
        boolean bidSuccessfull = false;

        while(attemptCount < MAX_BID_ATTEMPTS){
            try{
                bidSuccessfull = bidSaver.saveBidOnAuction(auctionId, bid);
                break;
            }catch(AuctionLockedException e){
                loggingService.logBidUnsuccessfull(auctionId, bid);
                attemptCount++;
            }

            waitABit();
        }

        return bidSuccessfull;
    }

    void waitABit(){}
}
