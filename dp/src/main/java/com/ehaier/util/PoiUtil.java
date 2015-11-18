package com.ehaier.util;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;

/**
 * 导出Excel工具类
 * 
 * @author cuipengfei
 *
 */
public class PoiUtil {

	public static HSSFWorkbook excelService(String title_Name,
			List<ExcelBean> titleList, List<HashMap<String, String>> contentList) {
		// 遍历查出的动态表头
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
		HSSFSheet sheet = hssfWorkbook.createSheet(title_Name);
		HSSFRow headRow1 = sheet.createRow(0);
		HSSFCell cell = headRow1.createCell(0);
		headRow1.setHeight((short) 540);
		cell.setCellType(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue(new HSSFRichTextString(title_Name));
		sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) titleList
				.size()));

		// 标题样式
		HSSFCellStyle cellStyle = hssfWorkbook.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION); // 指定单元格居中对齐
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐

		cellStyle.setWrapText(true);// 指定单元格自动换行
		// 设置标题格字体
		HSSFFont font = hssfWorkbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		font.setFontHeight((short) 400);
		cellStyle.setFont(font);
		cell.setCellStyle(cellStyle);

		// 首行字体
		HSSFFont headerFont = hssfWorkbook.createFont();
		// headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headerFont.setFontHeight((short) 250);

		// 首行的样式
		HSSFCellStyle headerStyle = hssfWorkbook.createCellStyle();
		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION); // 指定单元格居中对齐
		headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
		headerStyle.setFont(headerFont);
		headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
				.getIndex());
		headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

		// 内容字体
		HSSFFont contentFont = hssfWorkbook.createFont();
		// headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		contentFont.setFontName("宋体");
		contentFont.setFontHeight((short) 200);

		// 内容的样式
		HSSFCellStyle contentStyle = hssfWorkbook.createCellStyle();
		contentStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION); // 指定单元格居中对齐
		contentStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐

		contentStyle.setFont(contentFont);

		HSSFRow headRow = sheet.createRow(1);
		headRow.setHeight((short) 350);
		if (titleList != null && !"".equals(titleList)) {
			// 设置表头宽度
			for (int i = 0; i < titleList.size(); i++) {
				sheet.setColumnWidth(i, 30 * 200);
				HSSFCell headerCell = headRow.createCell(i);
				headerCell.setCellValue(titleList.get(i).getTitle());
				headerCell.setCellStyle(headerStyle);
			}
		}
		if (contentList != null && !"".equals(contentList)) {
			int j = 1;
			// 设置表头

			for (HashMap<String, String> hashMap : contentList) {

				j++;
				Set<String> keys = hashMap.keySet();
				HSSFRow dataRow = sheet.createRow(j);
				for (String key1 : keys) {
					for (int i = 0; i < titleList.size(); i++) {
						if (key1.equalsIgnoreCase(titleList.get(i).getField())) {
							Object val = hashMap.get(key1);
							HSSFCell contentCell = dataRow.createCell(i);
							if(val!=null&&!"".equals(val)){
								contentCell.setCellValue(val.toString());
							}else{
								contentCell.setCellValue("");
							}
							contentCell.setCellStyle(contentStyle);
						}
					}
				}
			}
		}
		return hssfWorkbook;
	}
}
