package com.sebox.diary.utils;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import android.os.Environment;

public class FileOperate {
	public static boolean wirteData(String fileName, String info) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			try {
				FileOutputStream outStream = new FileOutputStream("/sdcard/"
						+ fileName + ".txt", true);
				OutputStreamWriter writer = new OutputStreamWriter(outStream,
						"gbk");
				writer.write(info);
				writer.write("\n");
				writer.flush();
				writer.close();// ¼ÇµÃ¹Ø±Õ

				outStream.close();

			} catch (Exception e) {
				// TODO: handle exception
			}
			return true;
		} else {
			return false;
		}
	}
}
