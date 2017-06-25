package org.kelab.util;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import lombok.Data;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.kelab.util.constant.CharsetConstant;
import org.kelab.util.model.PairExt;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * Created by hongfei.whf on 2016/12/20.
 */
public class ExcelUtil {

	/**
	 * 创建excel导出类
	 *
	 * @param os
	 * @return
	 */
	public static Exporter exporter(@NonNull OutputStream os) {
		return new Exporter(os);
	}

	/**
	 * 创建excel导出类
	 *
	 * @param os
	 * @return
	 */
	public static CsvExporter csvExporter(@NonNull OutputStream os) {
		return new CsvExporter(os);
	}

	/**
	 * 创建excel解析类
	 *
	 * @param is
	 * @return
	 */
	public static Reader reader(@NonNull InputStream is) {
		return new Reader(is);
	}

	/**
	 * csv 导出封装类
	 */
	@Data
	public static class CsvExporter {

		/**
		 * 换行符
		 */
		private static final String CRLF = "\r\n";

		/**
		 * 输出流
		 */
		private OutputStream os;

		/**
		 * 构造函数
		 *
		 * @param os
		 */
		@SneakyThrows
		public CsvExporter(@NonNull OutputStream os) {
			this.os = os;
		}

		/**
		 * 添加记录
		 *
		 * @param records
		 * @return
		 */
		@SneakyThrows
		public CsvExporter writeRecords(@NonNull Object... records) {
			Collection collection = Arrays.asList(records);
			String line = StringUtil.join(collection, ",");
			os.write((line + CRLF).getBytes(CharsetConstant.UTF_8));
			return this;
		}
	}

	/**
	 * Excel 导出封装类
	 */
	@Data
	public static class Exporter {

		/**
		 * 工作簿
		 *
		 * @param cells
		 */
		private WritableWorkbook workbook;

		/**
		 * Excel创建新的一页的名字
		 */
		private Map<String, PairExt<WritableSheet, Integer/*写入第几行*/, Integer/*写入第几列*/>> sheets = new HashMap<>();

		/**
		 * 构造函数
		 *
		 * @param os
		 */
		@SneakyThrows
		public Exporter(@NonNull OutputStream os) {
			this.workbook = Workbook.createWorkbook(os);
		}

		/**
		 * 添加Sheet
		 */
		@SneakyThrows
		public Exporter addSheet(@NonNull String sheetName) {
			if (sheets.containsKey(sheetName)) throw new Exception("sheetName is duplicate");
			else {
				WritableSheet sheet = workbook.createSheet(sheetName, sheets.size() + 1);
				sheets.put(sheetName, new PairExt<WritableSheet, Integer, Integer>(sheet, 0, 0));
			}
			return this;
		}

		/**
		 * 添加cell
		 */
		@SneakyThrows
		public Exporter addCells(@NonNull String sheetName,
		                         @NonNull WritableCellFormat writableCellFormat,
		                         @NonNull Object... cells) {
			if (!sheets.containsKey(sheetName)) {
				addSheet(sheetName);
			}
			PairExt<WritableSheet, Integer/*rowNum*/, Integer/*colNum*/> sheetValue = sheets.get(sheetName);
			int rowNum = sheetValue.getValue2();
			int colNum = sheetValue.getValue3();
			for (Object cell : cells) {
				Label cellLabel = new Label(colNum++, rowNum, cell.toString(),
						writableCellFormat);
				(sheetValue.getValue1()).addCell(cellLabel);
			}
			sheetValue.setValue3(colNum);
			return this;
		}

		/**
		 * 添加cell
		 */
		@SneakyThrows
		public Exporter addCells(@NonNull String sheetName,
		                         @NonNull Object... cells) {
			if (!sheets.containsKey(sheetName)) {
				addSheet(sheetName);
			}
			PairExt<WritableSheet, Integer/*rowNum*/, Integer/*colNum*/> sheetValue = sheets.get(sheetName);
			int rowNum = sheetValue.getValue2();
			int colNum = sheetValue.getValue3();
			for (Object cell : cells) {
				Label cellLabel = new Label(colNum++, rowNum, cell.toString());
				(sheetValue.getValue1()).addCell(cellLabel);
			}
			sheetValue.setValue3(colNum);
			return this;
		}

		/**
		 * Excel 下一行
		 *
		 * @param sheetName
		 */
		public void nextLine(@NonNull String sheetName) {
			if (!sheets.containsKey(sheetName)) {
				addSheet(sheetName);
			}
			PairExt<WritableSheet, Integer/*rowNum*/, Integer/*colNum*/> sheetValue = sheets.get(sheetName);
			int rowNum = sheetValue.getValue2();
			sheetValue.setValue2(rowNum + 1);
			sheetValue.setValue3(0);
		}

		/**
		 * 写出到输出流
		 */
		@SneakyThrows
		public void write() {
			if (workbook != null) {
				workbook.write();
				workbook.close();
			}
		}
	}

	/**
	 * excel解析者
	 */
	public static class Reader {

		/**
		 * Excel
		 */
		private Workbook workbook = null;

		/**
		 * Excel
		 */
		private Map<String, Integer/*当前解析到第几行*/> sheets = new HashMap<>();

		/**
		 * 构造函数
		 *
		 * @param is
		 */
		@SneakyThrows
		public Reader(@NonNull InputStream is) {
			this.workbook = Workbook.getWorkbook(is);
		}

		/**
		 * 设置指定sheetName从哪一行开始解析
		 *
		 * @param sheetName
		 * @param fromRowNumber 从哪一行开始解析，0是第一行
		 * @return
		 */
		public Reader setParserSheetFromRow(@NonNull String sheetName, int fromRowNumber) {
			sheets.put(sheetName, fromRowNumber);
			return this;
		}

		/**
		 * 获取下一行的所有数据
		 *
		 * @return
		 */
		@SneakyThrows
		public List<String> next(@NonNull String sheetName) {
			Sheet sheet = this.workbook.getSheet(sheetName);
			int currRowNum = sheets.get(sheetName);
			if (currRowNum < sheet.getRows()) {
				List<String> data = new ArrayList<>();
				for (int i = 0, len = sheet.getColumns(); i < len; i++) {
					data.add(sheet.getCell(i, currRowNum).getContents());
				}
				sheets.put(sheetName, currRowNum + 1);
				return data;
			} else {
				throw new Exception("access error!");
			}
		}

		/**
		 * 获取下一行的所有数据
		 *
		 * @param sheetName
		 * @return
		 */
		@SneakyThrows
		public boolean hasNext(@NonNull String sheetName) {
			Sheet sheet = this.workbook.getSheet(sheetName);
			int currRowNum = sheets.get(sheetName);
			return currRowNum < sheet.getRows();
		}

	}
}
