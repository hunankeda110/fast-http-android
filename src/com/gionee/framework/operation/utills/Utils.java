package com.gionee.framework.operation.utills;

public class Utils {

    public static boolean isOddNumber(int position) {
        return (position % 2) != 0;
    }
    
    public static String formatFileLength(long fileLength) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (fileLength >= gb) {
            return String.format("%.1f GB", (float) fileLength / gb);
        } else if (fileLength >= mb) {
            float f = (float) fileLength / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (fileLength >= kb) {
            float f = (float) fileLength / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else
            return String.format("%d B", fileLength); 
    }
    
    /**
     * 判断对象是否非空
     * 
     * @param object
     * @return true:空 false:非空
     */
    public static boolean isNull(Object object) {
        if (object == null) {
            return true;
        }

        return false;
    }

    /**
     * 判断对象是否非空
     * 
     * @param object
     * @return true:非空 false:空
     */
    public static boolean isNotNull(Object object) {
        return !isNull(object);
    }
}
