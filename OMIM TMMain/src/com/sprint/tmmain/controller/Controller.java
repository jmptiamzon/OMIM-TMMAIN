package com.sprint.tmmain.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.sprint.tmmain.model.Model;
import com.sprint.tmmain.model.Tmmain;

public class Controller  {
	private Model model;
	private List<Tmmain> tmmainContent;
	private Tmmain tmmain;
	private String parameters;
	private Map<String, Integer> sqlRecord;
	
	public void runApp(String filename) {
		sqlRecord = new HashMap<>();
		model = new Model();
		tmmainContent = new ArrayList<>();
		parameters = "";
		
		try {
			FileInputStream xlsxFile = new FileInputStream(new File(filename));
			Workbook workbook = new XSSFWorkbook(xlsxFile);
			Sheet sheet = workbook.getSheetAt(0);
			
			
			Iterator<Row> iter = sheet.iterator();

			while (iter.hasNext()) {
				Row nextRow = iter.next();
				tmmain = new Tmmain();
				
				if (!nextRow.getCell(0).getStringCellValue().trim().equalsIgnoreCase("sku") && !nextRow.getCell(0).getStringCellValue().trim().equalsIgnoreCase("grand total")) {
					tmmain.setSku(nextRow.getCell(0).getStringCellValue().trim());
					parameters += "'" + nextRow.getCell(0).getStringCellValue().trim() + "',";
				}
				
				if (nextRow.getCell(1).getCellType() == CellType.NUMERIC) {
					tmmain.setQty(nextRow.getCell(1).getNumericCellValue());
				}
				
				tmmainContent.add(tmmain);
				
			}
			
			workbook.close();
			xlsxFile.close();
			
		} catch (IOException e) {
			System.out.println("File error: " + e.getMessage());
		}
		
		parameters = parameters.substring(0, parameters.length() - 1);
		sqlRecord = model.runTmmain(parameters);
		
		for (int ctr = 0; ctr < tmmainContent.size(); ctr++) {
			if (sqlRecord.containsKey(tmmainContent.get(ctr).getSku())) {
				tmmainContent.get(ctr).setTmmainQty(sqlRecord.get(tmmainContent.get(ctr).getSku()));
				tmmainContent.get(ctr).setDelta((int)tmmainContent.get(ctr).getQty() - sqlRecord.get(tmmainContent.get(ctr).getSku()));
			}
			
			System.out.println(tmmainContent.get(ctr).getSku() + " " + tmmainContent.get(ctr).getQty() + " " + tmmainContent.get(ctr).getTmmainQty() + " " + tmmainContent.get(ctr).getDelta());
		}
		
		
		
		
		
	}
}
