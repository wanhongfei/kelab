package org.kelab.test.util;

import org.apache.commons.io.FileUtils;
import org.kelab.util.ExcelUtil;

import java.io.File;
import java.io.IOException;

/**
 * Created by hongfei.whf on 2016/12/21.
 */
public class ExcelUtilTest {

	public static void main(String args[]) throws IOException {
//		OutputStream os = FileUtils.openOutputStream(new File("E:\\tmp\\test.csv"));
//		ExcelUtil.csvExporter(os)
//				.writeRecords("my worksheet", 1, 2, 3, 4, 5, 6, 7)
//				.writeRecords("my worksheet", "a", "a", "a", "a", "a", "a");
//		if (os != null) {
//			os.flush();
//			os.close();
//		}
//		InputStream is = FileUtils.openInputStream(new File("E:\\tmp\\test.xls"));
//		ExcelUtil.Reader reader = ExcelUtil.reader(is).setParserSheetFromRow("my worksheet", 0);
//		while (reader.hasNext("my worksheet")) {
//			System.out.println(reader.next("my worksheet"));
//		}
//		if (is != null) {
//			is.close();
//		}
		ExcelUtil.Exporter exporter = ExcelUtil.exporter(FileUtils.openOutputStream(new File("E:\\tmp\\test11.xls")));
		exporter.addSheet("test");
		exporter.addCells("test", 1, 2, 3)
				.addCells("test", 4, 5, 6)
				.nextLine("test");
		exporter.addCells("test", "a", "b", "c")
				.addCells("test", 4, 5, 6)
				.nextLine("test");
		exporter.write();
	}
}
