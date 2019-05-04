package com.sourceartists.auction.service.impl;

import com.sourceartists.auction.exception.AuctionLockedException;
import com.sourceartists.auction.exception.WatchingException;
import com.sourceartists.auction.model.*;
import com.sourceartists.auction.repository.AuctionRepository;
import com.sourceartists.auction.repository.BuyerRepository;
import com.sourceartists.auction.repository.SellerRepository;
import com.sourceartists.auction.service.AuctionService;
import com.sourceartists.auction.service.LoggingService;
import com.sourceartists.auction.service.MailService;
import com.sourceartists.auction.service.helper.BidSaver;
import com.sourceartists.auction.util.RatingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class BasicAuctionService implements AuctionService {

    public static final int MAX_NUMBER_OF_WATCHED_AUCTIONS = 4;
    @Autowired
    private BidSaver bidSaver;
    @Autowired
    private LoggingService loggingService;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private AuctionRepository auctionRepository;
    @Autowired
    private BuyerRepository buyerRepository;
    @Autowired
    private MailService mailService;

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

    @Override
    @Transactional
    public void rateSeller(Integer sellerId, Rating newRating) {
        Seller seller = sellerRepository.getOne(sellerId);
        List<Rating> sellerRatings = seller.getRatings();
        sellerRatings.add(newRating);

        BigDecimal newAverageRating = calculateRating(sellerRatings);

        seller.setAverageRating(newAverageRating);
    }

    @Override
    @Transactional
    public boolean watchAnOffer(Integer auctionId, Integer personId) throws WatchingException {
        Auction auction = auctionRepository.findById(auctionId).get();
        Buyer buyer = buyerRepository.findById(personId).get();

        if(buyer.getWatchedAuctions().size() > MAX_NUMBER_OF_WATCHED_AUCTIONS){
            throw new WatchingException("Max number of offers are already watched.");
        }

        for(Auction watchedAuction: buyer.getWatchedAuctions()) {
            if(watchedAuction.getId().equals(auctionId)){
                throw new WatchingException("Auction is already watched.");
            }
        }

        mailService.sendWatchConfirmationToUser(auctionId, personId);

        if(buyer.isSendNotificationToFriends()){
            mailService.sendWatchConfirmationToUserFriends(auctionId, personId);
        }

        return true;
    }

    BigDecimal calculateRating(List<Rating> ratings){
        return RatingUtils.calculateRating(ratings);
    }

    void waitABit(){}
}
