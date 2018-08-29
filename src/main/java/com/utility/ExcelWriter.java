/**
 *
 */
package com.utility;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * @author USER
 *
 */
public class ExcelWriter implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Workbook wb = null;
	private String fileName = null;

	public ExcelWriter() {
		super();
	}

	/**
	 *
	 * @param wb
	 */
	public ExcelWriter(Workbook wb) {
		this.wb = wb;
	}

	/**
	 *
	 * @return
	 */
	public Workbook getWorkBook() {
		return wb;
	}

	/**
	 *
	 * @param response
	 * @param fileName
	 * @throws IOException
	 */
	public void write(HttpServletResponse response, String fileName) throws IOException {
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
		response.setHeader("Pragma", "public");
		response.setHeader("Cache-Control", "max-age=0");

		ServletOutputStream toBrowser = response.getOutputStream();
		wb.write(toBrowser);
		toBrowser.flush();
	}

	/**
	 *
	 * @param fileName
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void write(String fileName) throws FileNotFoundException, IOException {
		synchronized (fileName) {
			FileOutputStream fileOut = new FileOutputStream(fileName);
			wb.write(fileOut);
			fileOut.close();
		}
	}

	/**
	 *
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void write() throws FileNotFoundException, IOException {
		synchronized (fileName) {
			FileOutputStream fileOut = new FileOutputStream(fileName);
			wb.write(fileOut);
			fileOut.close();
		}
	}

	/**
	 *
	 * @param sheetName
	 * @param rowNo
	 * @param columnNo
	 * @param halign
	 * @param valign
	 * @param value
	 */
	public void createCentralizedCell(String sheetName, int rowNo, int columnNo, HorizontalAlignment halign,
			VerticalAlignment valign, String value) {
		if (value == null) {
			return;
		}

		Sheet sheet = getSheet(sheetName);
		Row row = getRow(sheet, rowNo);
		Cell cell = getCell(row, columnNo);
		cell.setCellValue(value);

		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(halign);
		cellStyle.setVerticalAlignment(valign);
		cell.setCellStyle(cellStyle);
	}

	/**
	 *
	 * @param sheetName
	 * @param fromRowNo
	 * @param fromColumnNo
	 * @param toRowNo
	 * @param toColumnNo
	 * @param fontSize
	 * @param color
	 * @param fillPatternType
	 * @param highPoint
	 * @param value
	 * @param wrap
	 * @param bold
	 */
	public void createTitleRegionCentralizedCell(String sheetName, int fromRowNo, int fromColumnNo, int toRowNo,
			int toColumnNo, int fontSize, int color, FillPatternType fillPatternType, int highPoint, String value,
			boolean wrap, boolean bold) {
		if (value == null) {
			return;
		}

		Sheet sheet = getSheet(sheetName);
		Row row = getRow(sheet, fromRowNo);
		Cell cell = getCell(row, fromColumnNo);

		CellStyle style = wb.createCellStyle();
		setFontBold(style, fontSize, bold);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.BOTTOM);
		style.setFillBackgroundColor((short) color);
		style.setFillPattern(fillPatternType);
		style.setWrapText(wrap);

		row.setHeightInPoints(highPoint * sheet.getDefaultRowHeightInPoints());

		// TODO to be improved
		sheet.autoSizeColumn(2);

		setCellValue(cell, value);
		cell.setCellStyle(style);
	}

	/**
	 *
	 * @param sheetName
	 * @param fromRowNo
	 * @param fromColumnNo
	 * @param toRowNo
	 * @param toColumnNo
	 * @param value
	 * @param wrap
	 */
	public void appendTitleBoldMergeRegionValue(String sheetName, int fromRowNo, int fromColumnNo, int toRowNo,
			int toColumnNo, String value, boolean wrap) {
		if (value == null) {
			return;
		}

		Sheet sheet = getSheet(sheetName);
		Row row = getRow(sheet, fromRowNo);
		Cell cell = getCell(row, fromColumnNo);

		CellStyle style = wb.createCellStyle();
		setFontBold(style);
		style.setWrapText(wrap);

		setCellValue(cell, value);
		cell.setCellStyle(style);

		sheet.addMergedRegion(new CellRangeAddress(fromRowNo, toRowNo, fromColumnNo, toColumnNo));
	}

	/**
	 *
	 * @param sheetName
	 * @param rowNo
	 * @param columnNo
	 * @param value
	 * @param wrap
	 */
	public void appendTitleBoldValue(String sheetName, int rowNo, int columnNo, String value, boolean wrap) {
		if (value == null) {
			return;
		}

		Sheet sheet = getSheet(sheetName);
		Row row = getRow(sheet, rowNo);
		Cell cell = getCell(row, columnNo);

		CellStyle style = wb.createCellStyle();
		setFontBold(style);
		style.setWrapText(wrap);
		setCellValue(cell, value);
		cell.setCellStyle(style);
	}

	/**
	 *
	 * @param sheetName
	 * @param rowNo
	 * @param value
	 * @param wrap
	 */
	public void appendTitleBoldValues(String sheetName, int rowNo, String[] value, boolean wrap) {
		if ((value == null) || (value.length == 0)) {
			return;
		}

		Sheet sheet = getSheet(sheetName);
		Row row = getRow(sheet, rowNo);

		CellStyle style = wb.createCellStyle();
		setFontBold(style);
		style.setWrapText(wrap);

		for (int i = 0; i < value.length; i++) {
			Cell cell = getCell(row, i);
			setCellValue(cell, value[i]);
			cell.setCellStyle(style);

		}
	}

	/**
	 *
	 * @param sheetName
	 * @param rowNo
	 * @param columnNo
	 * @param value
	 */
	public void appendTitleCentralizedValue(String sheetName, int rowNo, int columnNo, String value) {
		if (value == null) {
			return;
		}

		Sheet sheet = getSheet(sheetName);
		Row row = getRow(sheet, rowNo);
		Cell cell = getCell(row, columnNo);

		CellStyle style = wb.createCellStyle();
		setFontBold(style, 11, true);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.BOTTOM);
		style.setWrapText(false);
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(FillPatternType.NO_FILL);

		setCellValue(cell, value);
		cell.setCellStyle(style);
	}

	/**
	 *
	 * @param sheetName
	 * @param rowNo
	 * @param columnNo
	 * @param fontSize
	 * @param color
	 * @param fillPatternType
	 * @param value
	 * @param wrap
	 * @param bold
	 */
	public void appendTitleCentralizedValue(String sheetName, int rowNo, int columnNo, int fontSize, int color,
			FillPatternType fillPatternType, String value, boolean wrap, boolean bold) {
		if (value == null) {
			return;
		}

		Sheet sheet = getSheet(sheetName);
		Row row = getRow(sheet, rowNo);
		Cell cell = getCell(row, columnNo);

		CellStyle style = wb.createCellStyle();
		setFontBold(style, fontSize, bold);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.BOTTOM);
		style.setWrapText(wrap);
		style.setFillForegroundColor((short) color);
		style.setFillPattern(fillPatternType);

		setCellValue(cell, value);
		cell.setCellStyle(style);
	}

	/**
	 *
	 * @param sheetName
	 * @param rowNo
	 * @param columnNo
	 * @param color
	 * @param fillPatternType
	 * @param value
	 * @param bold
	 */
	public void appendTitleCentralizedValue(String sheetName, int rowNo, int columnNo, int color,
			FillPatternType fillPatternType, String value, boolean bold) {
		if (value == null) {
			return;
		}

		Sheet sheet = getSheet(sheetName);
		Row row = getRow(sheet, rowNo);
		Cell cell = getCell(row, columnNo);

		CellStyle style = wb.createCellStyle();
		setFontBold(style, 11, bold);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.BOTTOM);
		style.setWrapText(false);
		style.setFillForegroundColor((short) color);
		style.setFillPattern(fillPatternType);

		setCellValue(cell, value);
		cell.setCellStyle(style);
	}

	/**
	 *
	 * @param style
	 * @param fontSize
	 * @param bold
	 */
	private void setFontBold(CellStyle style, int fontSize, boolean bold) {
		Font font = wb.createFont();
		font.setBold(bold);
		font.setFontHeightInPoints((short) fontSize);
		style.setFont(font);
	}

	/**
	 *
	 * @param sheetName
	 * @param value
	 */
	public void appendCellValues(String sheetName, Object[][] value) {
		if ((value == null) || (value.length == 0)) {
			return;
		}

		int lastRow = getLastRowNum(sheetName) + 1;
		for (Object[] element : value) {
			updateCellValues(sheetName, lastRow, element);
		}
	}

	/**
	 *
	 * @param sheetName
	 * @param rowNo
	 * @param cellNo
	 * @param value
	 */
	public void updateCellValueWithBold(String sheetName, int rowNo, int cellNo, Object value) {
		if (value == null) {
			return;
		}
		Sheet sheet = getSheet(sheetName);
		Row row = getRow(sheet, rowNo);
		Cell cell = getCell(row, cellNo);
		CellStyle style = wb.createCellStyle();
		setFontBold(style);
		setCellValue(cell, value);
		cell.setCellStyle(style);
	}

	/**
	 *
	 * @param style
	 */
	private void setFontBold(CellStyle style) {
		Font font = wb.createFont();
		font.setBold(true);
		style.setFont(font);
	}

	/**
	 *
	 * @param sheetIndex
	 * @param rowNo
	 * @param value
	 */
	public void updateCellValues(int sheetIndex, int rowNo, Object[] value) {
		updateCellValues(getSheetName(sheetIndex), rowNo, value);
	}

	/**
	 *
	 * @param sheetIndex
	 * @param rowNo
	 * @param cellNo
	 * @param value
	 */
	public void updateCellValue(int sheetIndex, int rowNo, int cellNo, Object value) {
		updateCellValue(getSheetName(sheetIndex), rowNo, cellNo, value);
	}

	/**
	 *
	 * @param sheetName
	 * @param rowNo
	 * @param cellNo
	 * @param value
	 */
	public void updateCellValue(String sheetName, int rowNo, int cellNo, Object value) {
		if (value == null) {
			return;
		}

		Sheet sheet = getSheet(sheetName);
		Row row = getRow(sheet, rowNo);
		Cell cell = getCell(row, cellNo);
		setCellValue(cell, value);
	}

	/**
	 *
	 * @param sheetIndex
	 * @param value
	 */
	public void updateCellValues(int sheetIndex, Object[][] value) {
		updateCellValues(getSheetName(sheetIndex), value);
	}

	/**
	 *
	 * @param sheetName
	 * @param value
	 */
	public void updateCellValues(String sheetName, Object[][] value) {
		if ((value == null) || (value.length == 0)) {
			return;
		}

		for (int i = 0; i < value.length; i++) {
			updateCellValues(sheetName, i, value[i]);
		}
	}

	/**
	 *
	 * @param sheetName
	 * @param rowNo
	 * @param value
	 */
	public void updateCellValues(String sheetName, int rowNo, Object[] value) {
		if ((value == null) || (value.length == 0)) {
			return;
		}

		Sheet sheet = getSheet(sheetName);
		Row row = getRow(sheet, rowNo);

		for (int i = 0; i < value.length; i++) {
			setCellValue(getCell(row, i), value[i]);
			setAutoColumnWidth(sheet, i, (String) value[i]);
		}
	}

	/**
	 *
	 * @param sheet
	 * @param column
	 * @param value
	 */
	public void setAutoColumnWidth(Sheet sheet, int column, String value) {
		if ((sheet != null) && (value != null)) {
			sheet.setColumnWidth(column, value.length() * 450);
		}
	}

	/**
	 *
	 * @param index
	 * @return
	 */
	public String getSheetName(int index) {
		return wb.getSheetName(index);
	}

	/**
	 *
	 * @param index
	 * @return
	 */
	public int getLastRowNum(int index) {
		int rowSize = 0;
		Sheet sheet = wb.getSheetAt(index);

		if (sheet != null) {
			rowSize = sheet.getLastRowNum();
		}
		return rowSize;
	}

	/**
	 *
	 * @param sheetName
	 * @return
	 */
	public int getLastRowNum(String sheetName) {
		int rowSize = 0;
		Sheet sheet = getSheet(sheetName);

		if (sheet != null) {
			rowSize = sheet.getLastRowNum();
		}
		return rowSize;
	}

	/**
	 *
	 * @param sheetName
	 * @param rowNo
	 */
	public void autoSizeColumnsByRowNo(String sheetName, int rowNo) {
		Sheet sheet = getSheet(sheetName);

		if (sheet.getPhysicalNumberOfRows() > 0) {
			Row row = sheet.getRow(rowNo);
			Iterator<Cell> cellIterator = row.cellIterator();

			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				int columnIndex = cell.getColumnIndex();
				sheet.autoSizeColumn(columnIndex);
			}
		}
	}

	/**
	 *
	 * @param sheetName
	 * @param rowNo
	 * @param columnIndex
	 * @param sizeOfWidth
	 */
	public void autoSizeColumns(String sheetName, int rowNo, int columnIndex, int sizeOfWidth) {
		Sheet sheet = getSheet(sheetName);

		if (sheet.getPhysicalNumberOfRows() > 0) {
			Row row = sheet.getRow(rowNo);
			Iterator<Cell> cellIterator = row.cellIterator();

			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				int colIndex = cell.getColumnIndex();

				if (colIndex >= columnIndex) {
					sheet.autoSizeColumn(colIndex);

				} else {
					int temp = columnIndex;
					sheet.setColumnWidth(--temp, sizeOfWidth);

					// TODO to be improved
					if (temp > 0) {
						sheet.setColumnWidth(--temp, sizeOfWidth);
					}
				}
			}
		}
	}

	/**
	 *
	 * @param sheetName
	 * @param rowNo
	 * @param cellNo
	 * @param value
	 */
	public void updateCellValueWithItalic(String sheetName, int rowNo, int cellNo, Object value) {
		if (value == null) {
			return;
		}

		Sheet sheet = getSheet(sheetName);
		Row row = getRow(sheet, rowNo);
		Cell cell = getCell(row, cellNo);

		Font font = wb.createFont();
		CellStyle style = wb.createCellStyle();
		setItalic(font);
		setCellValue(cell, value);
		style.setFont(font);
		cell.setCellStyle(style);
	}

	/**
	 *
	 * @param font
	 */
	private void setItalic(Font font) {
		font.setItalic(true);
	}

	/**
	 *
	 * @param sheetName
	 * @return
	 */
	private Sheet getSheet(String sheetName) {
		Sheet sheet = wb.getSheet(sheetName);
		if (sheet == null) {
			sheet = wb.createSheet(sheetName);
		}
		return sheet;
	}

	/**
	 *
	 * @param sheet
	 * @param rowNo
	 * @return
	 */
	private Row getRow(Sheet sheet, int rowNo) {
		Row row = sheet.getRow(rowNo);
		if (row == null) {
			row = sheet.createRow(rowNo);
		}
		return row;
	}

	/**
	 *
	 * @param row
	 * @param cellNo
	 * @return
	 */
	private Cell getCell(Row row, int cellNo) {
		Cell cell = row.getCell(cellNo);
		if (cell == null) {
			cell = row.createCell(cellNo);
		}
		return cell;
	}

	/**
	 *
	 * @param cell
	 * @param value
	 */
	private void setCellValue(Cell cell, Object value) {
		if ((cell != null) && (value != null)) {

			if (value instanceof Boolean) {
				Boolean objValue = (Boolean) value;
				cell.setCellValue(objValue.booleanValue());

			} else if (value instanceof Calendar) {
				Calendar objValue = (Calendar) value;
				cell.setCellValue(objValue);

			} else if (value instanceof Date) {
				Date objValue = (Date) value;
				cell.setCellValue(objValue);

			} else if (value instanceof Double) {
				Double objValue = (Double) value;
				cell.setCellValue(objValue);

			} else if (value instanceof String) {
				String objValue = (String) value;
				cell.setCellValue(objValue);

			} else {
				cell.setCellValue(value.toString());
			}
		}
	}

}
