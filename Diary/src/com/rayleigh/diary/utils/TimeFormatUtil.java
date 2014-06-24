package com.rayleigh.diary.utils;

public class TimeFormatUtil {
	public static String format(int x) {
		String s = "" + x;
		if (s.length() == 1)
			s = "0" + s;
		return s;
	}
}
