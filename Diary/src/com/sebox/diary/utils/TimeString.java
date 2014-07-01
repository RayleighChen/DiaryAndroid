package com.sebox.diary.utils;

import java.util.Calendar;


public class TimeString {
	public static String getTime(){
		long now = System.currentTimeMillis(); //΢�뼶��ǰʱ��  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTimeInMillis(now);
        String time = calendar.get(Calendar.YEAR)+""+(calendar.get(Calendar.MONTH) + 1)+
        		""+calendar.get(Calendar.DATE)+""+calendar.get(Calendar.HOUR)+""+
        		calendar.get(Calendar.MINUTE)+""+calendar.get(Calendar.SECOND);
		return time;
	}
}
