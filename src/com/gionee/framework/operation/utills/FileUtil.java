package com.gionee.framework.operation.utills;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * com.gionee.app_framework.operation.utills.FileUtil
 * 
 * @author yuwei <br/>
 *         create at 2013-3-18 上午9:38:27 TODO 常用文件读写操作
 */
public class FileUtil {

    /**
     * 从sd卡读对象
     * 
     * @param <U>
     * @param fileName
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <U> U readObjectFromSdcard(String folder, String fileName) {
        ObjectInputStream objectInputStream = null;
        U storeFileClass = null;
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File fileDir = new File(folder + fileName);
                if (fileDir.exists()) {
                    objectInputStream = new ObjectInputStream(new FileInputStream(fileDir));
                    storeFileClass = (U) objectInputStream.readObject();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (objectInputStream != null)
                    objectInputStream.close();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return storeFileClass;
    }

    /**
     * 从sd卡读对象
     * 
     * @param <U>
     * @param fileName
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <U> U readObjectFromSdcard(File fileDir) {
        ObjectInputStream objectInputStream = null;
        U storeFileClass = null;
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                if (fileDir.exists()) {
                    objectInputStream = new ObjectInputStream(new FileInputStream(fileDir));
                    storeFileClass = (U) objectInputStream.readObject();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (objectInputStream != null)
                    objectInputStream.close();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return storeFileClass;
    }

    /**
     * 写对象到sd卡
     * 
     * @param <U>
     * @param fileName
     * @param object
     */
    public static boolean writeObjectToSdcard(String folder, String fileName, Object object) {
        ObjectOutputStream objectOutputStream = null;
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File fileDir = new File(folder);
                if (!fileDir.exists()) {
                    if (!fileDir.mkdirs()) {
                        Log.i("GN_Gou_FileUtil", " dir make fail : " + fileDir);
                        return false;
                    };
                }

                objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File(folder + fileName)));
                objectOutputStream.writeObject(object);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (objectOutputStream != null)
                    objectOutputStream.close();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 删除文件
     * 
     * @param folder
     * @param fileName
     */
    public static boolean deleteFileFromSdcard(String folder, String fileName) {
        try {
            File deleteFile = new File(folder + fileName);
            if (deleteFile.exists()) {
                boolean isDeleted = deleteFile.delete();
                if (isDeleted) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除文件或文件夹
     * 
     * @param file
     */
    public static void deleteFile(File fileOrFolder) {
        if (fileOrFolder.exists()) { // 判断文件是否存在
            if (fileOrFolder.isFile()) { // 判断是否是文件
                fileOrFolder.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (fileOrFolder.isDirectory()) { // 否则如果它是一个目录
                File files[] = fileOrFolder.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            fileOrFolder.delete();
        } else {
            System.out.println("所删除的文件不存在！" + '\n');
        }
    }

    /**
     * 从data读对象
     * 
     * @param <U>
     * @param fileName
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <U> U readObjectFromLocation(Context context, String fileName) {
        ObjectInputStream objectInputStream = null;
        U storeFileClass = null;
        try {
            File deleteFile = context.getApplicationContext().getFileStreamPath(fileName);
            if (deleteFile.exists()) {
                objectInputStream = new ObjectInputStream(context.getApplicationContext().openFileInput(
                        fileName));
                storeFileClass = (U) objectInputStream.readObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (objectInputStream != null)
                    objectInputStream.close();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return storeFileClass;
    }

    /**
     * 写对象到data
     * 
     * @param <U>
     * @param fileName
     * @param object
     */
    public static boolean writeObjectToLocation(Context context, String fileName, Object object) {
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(context.getApplicationContext().openFileOutput(
                    fileName, Context.MODE_PRIVATE));
            objectOutputStream.writeObject(object);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (objectOutputStream != null)
                    objectOutputStream.close();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除对象文件
     * 
     * @param <U>
     * @param fileName
     */
    public static <U> boolean deleteFileFromLocation(Context context, String fileName) {
        try {
            File deleteFile = context.getApplicationContext().getFileStreamPath(fileName);
            if (deleteFile.exists()) {
                if (deleteFile.delete()) {
                    return true;
                };
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return false;
    }

}
