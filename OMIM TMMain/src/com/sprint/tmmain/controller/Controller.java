package com.sprint.tmmain.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.sprint.tmmain.model.Model;
import com.sprint.tmmain.model.Tmmain;

public class Controller  {
	private Model model;
	private List<Tmmain> tmmainContent;
	private Tmmain tmmain;
	private String parameters;
	private Map<String, Double> sqlRecord;
	private double totalTmmainQty, totalQty, totalDelta;
	
	public void runApp(String filename) {
		totalTmmainQty = 0;
		totalQty = 0;
		totalDelta = 0;
		sqlRecord = new HashMap<>();
		model = new Model();
		tmmainContent = new ArrayList<>();
		parameters = "";
		
		try {
			
			FileInputStream xlsxFile = new FileInputStream(new File(filename));
			Workbook workbook = new XSSFWorkbook(xlsxFile);
			Sheet sheet = workbook.getSheetAt(0);
			XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
			
			
			Iterator<Row> iter = sheet.iterator();

			while (iter.hasNext()) {
				Row nextRow = iter.next();
				
				if (!nextRow.getCell(0).getStringCellValue().trim().equalsIgnoreCase("sku") && !nextRow.getCell(0).getStringCellValue().trim().equalsIgnoreCase("grand total")) {
					tmmain = new Tmmain();
					tmmain.setSku(nextRow.getCell(0).getStringCellValue().trim());
					parameters += "'" + nextRow.getCell(0).getStringCellValue().trim() + "',";
					tmmain.setQty(nextRow.getCell(1).getNumericCellValue());
					tmmainContent.add(tmmain);
				}
				
			}
			
			// get data
			parameters = parameters.substring(0, parameters.length() - 1);
			sqlRecord = model.runTmmain(parameters);
			
			for (int ctr = 0; ctr < tmmainContent.size(); ctr++) {
				if (sqlRecord.containsKey(tmmainContent.get(ctr).getSku())) {
					tmmainContent.get(ctr).setTmmainQty(sqlRecord.get(tmmainContent.get(ctr).getSku()));
					tmmainContent.get(ctr).setDelta(sqlRecord.get(tmmainContent.get(ctr).getSku()) - tmmainContent.get(ctr).getQty());
				} else {
					tmmainContent.get(ctr).setDelta(0.0 - tmmainContent.get(ctr).getQty());
				}
				
			}
			
			//write
			Row row;
			for (int ctr = 0; ctr < tmmainContent.size(); ctr++) {
				totalTmmainQty += tmmainContent.get(ctr).getTmmainQty();
				totalQty += tmmainContent.get(ctr).getQty();
				
				row = sheet.createRow(ctr + 1);
				
				style.setBorderTop(BorderStyle.THIN);
				style.setBorderBottom(BorderStyle.THIN);
				style.setBorderRight(BorderStyle.THIN);
				style.setBorderLeft(BorderStyle.THIN);
				
				Cell cell = row.createCell(2);
				cell.setCellValue((int)tmmainContent.get(ctr).getTmmainQty());
				cell.setCellStyle(style);
				
				cell = row.createCell(0);
				cell.setCellValue(tmmainContent.get(ctr).getSku());
				cell.setCellStyle(style);
				
				cell = row.createCell(1);
				cell.setCellValue(tmmainContent.get(ctr).getQty());
				cell.setCellStyle(style);
				
				cell = row.createCell(3);
				cell.setCellValue((int)tmmainContent.get(ctr).getDelta());
				cell.setCellStyle(style);
			}
			
			row = sheet.createRow(tmmainContent.size() + 1);
			Cell cell = row.createCell(0);
			cell.setCellValue("Grand Total");
			cell.setCellStyle(style);
			
			cell = row.createCell(1);
			cell.setCellValue((int)totalQty);
			cell.setCellStyle(style);
			
			cell = row.createCell(2);
			cell.setCellValue((int)totalTmmainQty);
			cell.setCellStyle(style);
			
			cell = row.createCell(3);
			totalDelta = totalTmmainQty - totalQty;
			cell.setCellValue((int)totalDelta);
			cell.setCellStyle(style);
			
			xlsxFile.close();
			
			FileOutputStream fileOutputStream = new FileOutputStream(filename);
			workbook.write(fileOutputStream);
			workbook.close();
			fileOutputStream.close();
			
		} catch (IOException e) {
			System.out.println("File error: " + e.getMessage());
		}
		
	}
}
