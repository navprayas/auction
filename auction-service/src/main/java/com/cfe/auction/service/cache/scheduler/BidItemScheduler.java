package com.cfe.auction.service.cache.scheduler;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cfe.auction.service.cache.IBidItemsCacheService;

public class BidItemScheduler {
    private static final Logger logger = LoggerFactory.getLogger(BidItemScheduler.class);
    
    public long bidSpan;
    TimerTask task = null;
    int counter = 0;
    Timer timer = null;
    boolean scheduleNextItem = true;
    IBidItemsCacheService cacheService = null;
    
    public void setBidSpan(long l) {
        bidSpan = l;
    }
    
    public void stop() {
        scheduleNextItem = false;
    }
    
    public void setCacheService(IBidItemsCacheService c) {
        cacheService = c;
    }
    
    public void start(Date startTime) {

        task = new TimerTask() {
            @Override
            public void run() {
                try {
                    long span = cacheService.setNextActiveBidItem();
                    if(span == 0) {
                        timer.cancel();
                        return;
                    }
                    logger.debug("LOAD BIDITEM TO REDIS CACHE AND GOING TO SLEEP FOR THE SPAN OF TIME");
                    logger.debug("Going to sleep for ..." + span + " seconds...");
                    Thread.sleep(TimeUnit.SECONDS.toMillis(span));
                }
                catch(Exception e) {
                    logger.error(e.getMessage(), e);
                    e.printStackTrace();
                    timer.cancel();
                    return;
                }
            }
        };

        timer = new Timer();
        timer.schedule(task, startTime, TimeUnit.MILLISECONDS.toMillis(1000L));
    }
}