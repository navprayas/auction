package com.cfe.auction.web.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Row;

import com.cfe.auction.common.Bid;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFWriterUtils {
	private static final String pdfName = "ObserverSummary1Report.pdf";

	public static void getPDFFile(List<Bid> bidList,ServletOutputStream os) throws DocumentException,
			IOException {
		Document document = new Document();
		PdfWriter.getInstance(document,os);

		document.open();

		Chunk chunk = new Chunk("Observer Summary Report");
		Font font = new Font();

		font.setStyle(Font.UNDERLINE);
		font.setStyle(Font.ITALIC);
		font.setSize(9);
		chunk.setFont(font);
		document.add(chunk);
		Paragraph paragraph = new Paragraph();
		paragraph.add("Summary Report");
		paragraph.setAlignment(Element.ALIGN_CENTER);
		document.add(paragraph);
		document.add(new Chunk(" "));
		PdfPTable table = new PdfPTable(13); // 13 columns.
		table.setTotalWidth(350);
		table.setWidthPercentage(100);
		int[] a = { 3, 3, 3, 5, 4, 3, 4, 3, 3, 3, 3, 3, 3 };
		table.setWidths(a);
		table.addCell(getCell("Sr. No.", Element.ALIGN_CENTER));
		table.addCell(getCell("Lot No", Element.ALIGN_CENTER));
		table.addCell(getCell("Category", Element.ALIGN_CENTER));
		table.addCell(getCell("Material Name", Element.ALIGN_CENTER));
		table.addCell(getCell("Remark", Element.ALIGN_CENTER));
		table.addCell(getCell("Length Range", Element.ALIGN_CENTER));
		table.addCell(getCell("Quantity", Element.ALIGN_CENTER));
		table.addCell(getCell("Location", Element.ALIGN_CENTER));
		table.addCell(getCell("City", Element.ALIGN_CENTER));
		table.addCell(getCell("Zone", Element.ALIGN_CENTER));
		table.addCell(getCell("Currency", Element.ALIGN_CENTER));
		table.addCell(getCell("Lot's Status", Element.ALIGN_CENTER));
		table.addCell(getCell("Sales Price", Element.ALIGN_CENTER));
		int rowCount = 1;

		for (Bid bid : bidList) {
			table.addCell(getCell((rowCount++)+"", Element.ALIGN_CENTER));
			table.addCell(getCell((bid.getBidItemId())+"", Element.ALIGN_CENTER));
			table.addCell(getCell((bid.getCategoryName())+"", Element.ALIGN_CENTER));
			table.addCell(getCell((bid.getBidItemName())+"", Element.ALIGN_CENTER));
			table.addCell(getCell((bid.getComments())+"", Element.ALIGN_CENTER));
			table.addCell(getCell((bid.getUnit())+"", Element.ALIGN_CENTER));
			table.addCell(getCell((bid.getTotalQuantity())+"", Element.ALIGN_CENTER));
			table.addCell(getCell((bid.getLocation())+"", Element.ALIGN_CENTER));
			table.addCell(getCell((bid.getCity())+"", Element.ALIGN_CENTER));
			table.addCell(getCell((bid.getZone())+"", Element.ALIGN_CENTER));
			table.addCell(getCell((bid.getCurrency())+"", Element.ALIGN_CENTER));
			table.addCell(getCell((bid.getBidStatus())+"", Element.ALIGN_CENTER));
			table.addCell(getCell((bid.getSalesPrice())+"", Element.ALIGN_CENTER));
		}
		
		document.add(table);
		document.close();
		
	}

	private static PdfPCell getCell(String content, int alignment) {
		// PdfPCell cell = new PdfPCell(new Phrase(content));
		if (content == null || content.equalsIgnoreCase("null")) {
			content = "";
		}
		Font font = new Font();
		font.setSize(8);
		Chunk chunk = new Chunk(content);
		chunk.setFont(font);
		PdfPCell cell = new PdfPCell(new Paragraph(chunk));
		cell.setVerticalAlignment(alignment);
		return cell;
	}

}
