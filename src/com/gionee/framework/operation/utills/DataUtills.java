/*
 * DataUtills.java
 * classes : com.gionee.app_framework.operation.utills.DataUtills
 * @author yuwei
 * V 1.0.0
 * Create at 2013-3-18 上午9:42:56
 */
package com.gionee.framework.operation.utills;

import java.util.Date;

/**
 * com.gionee.app_framework.operation.utills.DataUtills
 * 
 * @author yuwei <br/>
 *         create at 2013-3-18 上午9:42:56 TODO
 */
public class DataUtills {
    public static String getTime(String timeStr) {
        if (timeStr.matches("^[0-9]*$")) {

            int time = Integer.parseInt(timeStr);
            if (time > 0 && time <= 10) {
                String[] times = new String[] {"一", " 二", "三", "四", " 五", "六", "七", "八", " 九", "十"};
                return times[time] + "年以上";
            } else
                return null;

        } else
            return timeStr;

    }

    public static String timeStamp2Date(String timestampString) {
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm")
                .format(new java.util.Date(timestamp));
        return date;
    }

    public static String formatDuring(String dateStr) {
        Date date = new Date(dateStr);
        long lastupdate = date.getTime();
        long current = System.currentTimeMillis();
        String time;
        long mss = current - lastupdate;
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        if (days > 0) {
            time = timeStamp2Date(lastupdate / 1000 + "");
        } else if (hours > 0) {
            time = hours + "小时" + minutes + "分钟前";
        } else if (minutes > 0) {
            time = minutes + "分钟前";
        } else {
            time = "刚刚";
        }
        return time;

    }

}
