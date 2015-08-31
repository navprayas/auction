/*    */ import com.navprayas.bidding.engine.common.Bid;
/*    */ import com.navprayas.bidding.engine.common.BidItem;
/*    */ import com.navprayas.bidding.engine.core.Auctioneer;
/*    */ import com.navprayas.bidding.engine.core.AuctioneerFactory;
/*    */ import com.navprayas.bidding.engine.core.BidderFactory;
/*    */ import com.navprayas.bidding.engine.intf.Bidder;
/*    */ import java.util.Calendar;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Client
/*    */ {
/*    */   public static BidItem item1;
/*    */   public static BidItem item2;
/*    */   
/*    */   public static void main(String[] args)
/*    */     throws Exception
/*    */   {
/* 44 */     BidItem item = new BidItem(1L);
/* 45 */     item.setStatusCode("ACTIVE");
/* 46 */     Calendar cal = Calendar.getInstance();
/* 47 */     item.setBidStartTime(cal.getTime());
/* 48 */     cal.add(10, 10);
/* 49 */     item.setBidEndTime(cal.getTime());
/* 50 */     item.setAuctionId(2011L);
/* 51 */     item.setMinBidPrice(2000.0D);
/* 52 */     item.setMinBidIncrement(200.0D);
/* 53 */     item.setTimeExtAfterBid(300);
/* 54 */     item.setMarketType(1);
/* 55 */     item.setCurrency("INR");
/*    */     
/* 57 */     Auctioneer acutioneer = AuctioneerFactory.create();
/* 58 */     acutioneer.startAuction();
/* 59 */     acutioneer.registerItem(item);
/*    */     
/*    */ 
/*    */ 
/* 63 */     Bid bid = new Bid("Jeeva", 2011L, 1L, Double.parseDouble(args[0]), "INR", "REQUEST");
/* 64 */     try { BidderFactory.create().call(bid);
/*    */     } catch (Exception localException) {}
/* 66 */     System.exit(0);
/*    */   }
/*    */ }


/* Location:              /home/cfeindia/Desktop/bidding-engine.jar!/Client.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */