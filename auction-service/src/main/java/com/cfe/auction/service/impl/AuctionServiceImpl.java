package com.cfe.auction.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cfe.auction.dao.IAuctionDao;
import com.cfe.auction.model.auction.persist.AuctionSearchBean;
import com.cfe.auction.model.persist.Auction;
import com.cfe.auction.model.persist.BidItem;
import com.cfe.auction.model.persist.BidSequence;
import com.cfe.auction.model.persist.Category;
import com.cfe.auction.model.persist.ItemLot;
import com.cfe.auction.service.BidItemService;
import com.cfe.auction.service.IAuctionService;

/**
 * 
 * @author Vikas Anand
 * 
 */
@Service("auctionServiceImpl")
@Transactional
public class AuctionServiceImpl extends
		CRUDServiceImpl<Integer, Auction, IAuctionDao> implements
		IAuctionService {

	@Autowired
	IAuctionDao auctionDao;

	@Autowired
	BidItemService bidItemService;

	@Autowired
	public AuctionServiceImpl(IAuctionDao dao) {
		super(dao);
	}

	@Transactional
	@Override
	public List<BidItem> getActiveAuctionBidItem(
			AuctionSearchBean auctionSearchBean) {
		List<BidItem> bidItems = bidItemService
				.getBidItemsbyAuctionId(auctionSearchBean);
		return bidItems;
	}

	@Transactional
	@Override
	public Auction getActiveAuction(AuctionSearchBean auctionSearchBean) {
		return auctionDao.getActiveAuction(auctionSearchBean);
	}

	@Transactional
	@Override
	public Auction getActiveAuction() {
		return auctionDao.getActiveAuction();
	}

	@Transactional
	@Override
	public List<Auction> getAuctionList(String schemaKey) {
		return auctionDao.getAuctionList(schemaKey);
	}

	@Override
	public void setActualAuctionStartTime(Integer auctionId,
			Date actualAuctionStartTime) {
		auctionDao.setActualAuctionStartTime(auctionId, actualAuctionStartTime);
	}

	@Override
	public void closeAuction(AuctionSearchBean auctionSearchBean) {
		auctionDao.closeAuction(auctionSearchBean);

	}

	@Override
	public boolean isValidAuction(AuctionSearchBean auctionSearchBean) {
		// TODO Auto-generated method stub
		return auctionDao.isValidAuction(auctionSearchBean);
	}

	@Override
	public void saveAuctionData(MultipartFile file,
			AuctionSearchBean auctionSearchBean) throws IOException {

		ByteArrayInputStream inputStream = (ByteArrayInputStream) file
				.getInputStream();
		POIFSFileSystem fs = new POIFSFileSystem(inputStream);
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		HSSFSheet sheet = wb.getSheetAt(0);
		Row row;

		Auction createAuction = new Auction();
		createAuction.setName(sheet.getSheetName());
		createAuction.setIsApproved("1");
		createAuction.setStatus("Start");
		createAuction.setBidItemGroupId(1L);
		createAuction.setCreatedTime(new Timestamp(new Date().getTime()));
		Auction auction = auctionDao.create(createAuction);
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			row = sheet.getRow(i);
			ItemLot itemLotEntity = new ItemLot();
			BidItem bidItemEntity = new BidItem();

			// Biditem table data
			// bidItemEntity.setAuctionId(selectedAuctionId);
			bidItemEntity.setName(row.getCell(4).getStringCellValue());
			bidItemEntity.setLocation(row.getCell(15).getStringCellValue());
			bidItemEntity.setCity(row.getCell(16).getStringCellValue());
			// .setZone(row.getCell(9).getStringCellValue());
			bidItemEntity.setMinBidPrice(row.getCell(11).getNumericCellValue());
			bidItemEntity.setMinBidIncrement(row.getCell(12)
					.getNumericCellValue());
			bidItemEntity.setBidStartTime(null);
			bidItemEntity.setBidEndTime(null);
			bidItemEntity.setTimeExtAfterBid((int) row.getCell(13)
					.getNumericCellValue());
			bidItemEntity.setStatusCode("Start");
			bidItemEntity.setCategoryId(2);
			bidItemEntity.setMarketId(1);
			bidItemEntity.setCurrency(row.getCell(14).getStringCellValue());
			bidItemEntity.setCreatedTime(new Timestamp(new Date().getTime()));
			bidItemEntity.setSerialNo(Double.toString(row.getCell(0)
					.getNumericCellValue()));
			bidItemEntity.setCurrentMarketPrice(row.getCell(10)
					.getNumericCellValue());
			bidItemEntity.setIsProcessed(null);
			bidItemEntity.setInitialStartTime(3);
			bidItemEntity.setNocolum(row.getCell(6).getStringCellValue());

			// Itemlot table data
			itemLotEntity.setName(row.getCell(4).getStringCellValue());
			/*
			 * itemLotEntity.setLotId(bidItemEntity.getBidItemId());
			 * itemLotEntity.setBidItemId(bidItemEntity.getBidItemId());
			 */
			itemLotEntity.setLengthRange(row.getCell(7).getStringCellValue());
			itemLotEntity.setActualLengh(null);
			itemLotEntity.setQuantity((int) row.getCell(8)
					.getNumericCellValue());
			itemLotEntity.setUnit(null);
			itemLotEntity.setCurrency(row.getCell(14).getStringCellValue());
			itemLotEntity.setRemark(row.getCell(5).getStringCellValue());
			itemLotEntity.setZone(row.getCell(9).getStringCellValue());
			itemLotEntity.setLotNo(String.valueOf(row.getCell(3)
					.getNumericCellValue()));

			BidSequence bidSequence = new BidSequence();
			bidSequence.setAuction(createAuction);
			if (auction != null) {
				bidItemEntity.setBidItemGroupId(Long.parseLong(auction
						.getBidItemGroupId().toString()));

				auctionDao.saveAuctionDataDao(itemLotEntity, bidItemEntity,
						bidSequence, auctionSearchBean);
			}
		}

	}
}
