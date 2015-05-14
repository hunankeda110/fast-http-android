package com.gionee.framework.operation.page;

import java.io.File;
import java.util.Properties;

import android.os.Environment;

import com.gionee.framework.operation.utills.FileUtil;

/**
 * com.gionee.app_framework.operation.page.FrameWorkInit
 * 
 * @author yuwei <br/>
 *         create at 2013-4-1 下午4:31:59 TODO init the configs of this framework
 */
public final class FrameWorkInit {
    // Gionee <yuwei><2012-5-27> add for CR00821559 begin
    public static final String SDCARD = Environment.getExternalStorageDirectory().getPath();
    public static final String imgPath = SDCARD + "/GN_GOU/imgcache/";

//	private static Properties config;

    public final static boolean clearImgCache() {
        File file = null;
        file = new File(imgPath);
        FileUtil.deleteFile(file);
        return true;
    }

    /**
   * 
   */
    public static void init(Properties config) {
        // if (FrameWorkInit.config == null) {
        // FrameWorkInit.config = config;
        // imgPath = SDCARD + getConfig(ConfigKey.IMAGECACHESDFOLDER,
        // "/imgcache/");
        // }
    }

//	/**
//	 * 获取配置
//	 * 
//	 * @param configKey
//	 * @return
//	 */
//	public static String getConfig(String configKey) {
//		if (config != null) {
//			return config.getProperty(configKey, "");
//		}
//		return null;
//	}
//
//	/**
//	 * 获取配置
//	 * 
//	 * @param configKey
//	 * @param defaultValue
//	 *            默认值
//	 * @return
//	 */
//	public static String getConfig(String configKey, String defaultValue) {
//		String value = getConfig(configKey);
//		return TextUtils.isEmpty(value) ? defaultValue : value;
//	}
    // Gionee <yuwei><2012-6-2> add for CR00821559 end
}
