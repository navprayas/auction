package com.cfe.auction.web.util;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import com.cfe.auction.common.Bid;

@Component
public class ExcelWriterUtils {

	/**
	 * This method is used to generate Excel Sheet for Basic Employer
	 */
	public static Workbook getExcelFile(List<Bid> bidList) {
		System.out.println("bidList" + bidList);

		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet("Bids Report");

		Row headerRow = sheet.createRow((short) 0);
		headerRow.createCell((short) 0).setCellValue(
				new HSSFRichTextString("Sr. No"));
		headerRow.createCell((short) 1).setCellValue(
				new HSSFRichTextString("Lot. No"));
		headerRow.createCell((short) 2).setCellValue(
				new HSSFRichTextString("Category"));
		headerRow.createCell((short) 3).setCellValue(
				new HSSFRichTextString("Material Name"));
		headerRow.createCell((short) 4).setCellValue(
				new HSSFRichTextString("Remark"));
		headerRow.createCell((short) 5).setCellValue(
				new HSSFRichTextString("Length Range"));
		headerRow.createCell((short) 6).setCellValue(
				new HSSFRichTextString("Quantity"));
		headerRow.createCell((short) 7).setCellValue(
				new HSSFRichTextString("Location"));
		headerRow.createCell((short) 8).setCellValue(
				new HSSFRichTextString("City"));
		headerRow.createCell((short) 9).setCellValue(
				new HSSFRichTextString("Zone"));
		headerRow.createCell((short) 10).setCellValue(
				new HSSFRichTextString("Currency"));
		headerRow.createCell((short) 11).setCellValue(
				new HSSFRichTextString("Lot's Status"));
		headerRow.createCell((short) 12).setCellValue(
				new HSSFRichTextString("Sales Price"));
		int rowCount = 1;

		for (Bid bid : bidList) {
			Row bidRow = sheet.createRow(rowCount);
			int columnCount = 0;

			Cell bidIdCell = bidRow.createCell((short) columnCount++);
			bidIdCell.setCellValue(rowCount++);
			Cell lotNoCell = bidRow.createCell((short) columnCount++);
			lotNoCell.setCellValue(bid.getBidItemId());

			Cell categoryCell = bidRow.createCell((short) columnCount++);
			categoryCell.setCellValue(bid.getCategoryName());

			Cell bidItemName = bidRow.createCell((short) columnCount++);
			bidItemName.setCellValue(bid.getBidItemName());
			Cell comments = bidRow.createCell((short) columnCount++);
			comments.setCellValue(bid.getComments());
			Cell unit = bidRow.createCell((short) columnCount++);
			unit.setCellValue(bid.getUnit());
			/*Cell materialName = bidRow.createCell((short) columnCount++);
			materialName.setCellValue(bid.getBidItemName());*/
			Cell totalQuantity = bidRow.createCell((short) columnCount++);
			totalQuantity.setCellValue(bid.getTotalQuantity());
			Cell location = bidRow.createCell((short) columnCount++);
			location.setCellValue(bid.getLocation());
			Cell city = bidRow.createCell((short) columnCount++);
			city.setCellValue(bid.getCity());
			Cell zone = bidRow.createCell((short) columnCount++);
			zone.setCellValue(bid.getZone());
			Cell currency = bidRow.createCell((short) columnCount++);
			currency.setCellValue(bid.getCurrency());
			Cell bidStatus = bidRow.createCell((short) columnCount++);
			bidStatus.setCellValue(bid.getBidStatus());
			Cell salesPrice = bidRow.createCell((short) columnCount++);
			salesPrice.setCellValue(bid.getSalesPrice());
		}
		return workbook;

	}

}
