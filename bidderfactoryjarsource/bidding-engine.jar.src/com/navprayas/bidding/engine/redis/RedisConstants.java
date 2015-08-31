package com.navprayas.bidding.engine.redis;

public abstract interface RedisConstants
{
  public static final String BIDITEMS = "::BIDITEMS::";
  public static final String BIDITEM = "BIDITEM::";
  public static final String BIDDERS = "::BIDDERS::";
  public static final String BIDDER = "BIDDER::";
  public static final String CATEGORIES = "::CATEGORIES::";
  public static final String CATEGORY = "CATEGORY::";
  public static final String BID_CATEGORY = "BID::CATEGORY::";
  public static final String DATESTRING = "yyMMddHHmmssZ";
  public static final String ATTR_BIDITEMID = "bidItemId";
  public static final String ATTR_NAME = "name";
  public static final String ATTR_LOCATION = "location";
  public static final String ATTR_CITY = "city";
  public static final String ATTR_ZONE = "zone";
  public static final String ATTR_VERSION = "version";
  public static final String ATTR_MINBIDPRICE = "minBidPrice";
  public static final String ATTR_MINBIDINCREMENT = "minBidIncrement";
  public static final String ATTR_BIDSTARTTIME = "bidStartTime";
  public static final String ATTR_BIDENDTIME = "bidEndTime";
  public static final String ATTR_INITIALSTARTTIME = "initialStartTime";
  public static final String ATTR_TIMEEXTAFTERBID = "timeExtAfterBid";
  public static final String ATTR_STATUSCODE = "statusCode";
  public static final String ATTR_WINNERID = "winnerId";
  public static final String ATTR_LASTUPDATETIME = "lastUpDateTime";
  public static final String ATTR_CREATEDTIME = "createdTime";
  public static final String ATTR_CURRENTMARKETPRICE = "currentMarketPrice";
  public static final String ATTR_ACTUALENDTIME = "actualEndTime";
  public static final String ATTR_DOCUMENT = "document";
  public static final String ATTR_CURRENCY = "currency";
  public static final String ATTR_ISAUTOBID = "isAutoBid";
  public static final String ATTR_AUTOBIDID = "AUTOBIDID";
  public static final String ATTR_AUTOBIDAMOUNT = "autoBidAmount";
  public static final String ATTR_CATEGORYID = "categoryId";
  public static final String ATTR_AUCTIONID = "auctionId";
  public static final String ATTR_AUTOBIDDERNAME = "autoBidderName";
  public static final String ATTR_CURRENTAUTOBIDID = "currentAutoBidId";
  public static final String ATTR_BIDSEQUENCEID = "seqId";
  public static final String ATTR_CATEGORY_NAME = "categoryName";
  public static final String ATTR_CATEGORY_ID = "categoryId";
  public static final String BID = "BID::BIDITEM::";
  public static final String ATTR_BIDTIME = "bidTime";
  public static final String ATTR_BIDDERID = "bidderId";
  public static final String ATTR_BIDDERNAME = "bidderName";
  public static final String ATTR_BIDSPAN = "bidSpan";
  public static final String BIDITEMID_SEQUENCE = "::BIDITEMID::SEQUENCE::";
  public static final String BIDSEQUENCE = "BIDSEQUENCE::";
  public static final String ATTR_SEQUENCEID = "sequenceId";
  public static final String BIDITEMSEQUENCEIDS = "BIDITEM::SEQUENCE::IDS";
  public static final String ACTIVE_BIDITEM = "ACTIVE::BIDITEM";
  public static final String ACTIVE_BIDITEMID = "ACTIVE::BIDITEM::ID";
  public static final String ENDSEQUNCE = "END::SEQUNCE";
  public static final String AUCTIONID = "::AUCTIONID::";
  public static final String EXPIRED_BIDITEMS = "::EXPIRED::BIDITEMS::";
  public static final String AUCTIONEND = "AUCTION::END";
}


/* Location:              /home/cfeindia/Desktop/bidding-engine.jar!/com/navprayas/bidding/engine/redis/RedisConstants.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */