package com.cloud.objects.storage;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.cloud.objects.ObjectJudge;
import com.cloud.objects.config.RxAndroid;
import com.cloud.objects.logs.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StorageUtils {

    /**
     * 创建主目录
     *
     * @return
     */
    public static File getRootDir() {
        RxAndroid.RxAndroidBuilder builder = RxAndroid.getInstance().getBuilder();
        String rootDirName = builder.getRootDirName();
        if (ObjectJudge.isEmptyString(rootDirName)) {
            rootDirName = "cloudapp";
        }
        File dir = null;
        if (ObjectJudge.hasSDCard()) {
            dir = createDirectory(new File(builder.getExternalCacheRootDir()), rootDirName);
        } else {
            dir = createDirectory(new File(builder.getInternalCacheRootDir()), rootDirName);
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 获取子目录 如果不存在，则自动创建
     *
     * @param destDirName 子目录名称
     * @return
     */
    public static File getDir(String destDirName) {
        File dir = createDirectory(getRootDir(), destDirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 创建目录
     * <p>
     * param dir  主目录
     * param dest 需要创建的子目录
     * return
     */
    public static File createDirectory(File dir, String dest) {
        File result = new File(dir, File.separator + dest + File.separator);
        if (!result.exists()) {
            result.mkdirs();
        }
        return result;
    }

    /**
     * 获取文件
     *
     * @param dir      目录
     * @param fileName 文件名称
     * @param delete   如果存在是否删除原文件重新创建
     * @return File
     */
    public static File getFile(File dir, String fileName, boolean delete) {
        File file = new File(dir, fileName);
        try {
            if (file.exists()) {
                if (delete) {
                    if (file.delete()) {
                        if (!file.createNewFile()) {
                            Logger.info(String.format("文件创建失败%s", file.getAbsolutePath()));
                        }
                    }
                }
            } else {
                if (!file.createNewFile()) {
                    Logger.info(String.format("文件创建失败%s", file.getAbsolutePath()));
                }
            }
        } catch (IOException e) {
            Logger.error(e);
        }
        return file;
    }

    /**
     * 删除目录或者文件
     * <p>
     * param filepath 文件路径
     * return
     */
    public static boolean deleteQuietly(String filePath) {
        File file = new File(filePath);
        return deleteQuietly(file);
    }

    /**
     * 删除目录或者文件
     * <p>
     * param dir      主目录
     * param filepath 文件名称
     * return
     */
    public static boolean deleteQuietly(String dir, String fileName) {
        File file = new File(dir, fileName);
        return deleteQuietly(file);
    }

    /**
     * 删除目录或者文件。 目录则递归删除， 文件直接删除
     * <p>
     * param file
     * return
     */
    public static boolean deleteQuietly(File file) {
        if (file == null) {
            return false;
        }
        try {
            if (file.isDirectory()) {
                cleanDirectory(file);
            }
        } catch (Exception ignored) {
        }

        try {
            return file.delete();
        } catch (Exception ignored) {
            return false;
        }
    }

    /**
     * 删除目录中的文件及目录 支持递归删除 删除目录本身
     * <p>
     * param directory
     */
    public static void deleteDirectory(File directory) {
        if (!directory.exists()) {
            return;
        }
        if (!isSymlink(directory)) {// 不是符号链接的，递归删除
            cleanDirectory(directory);
        }
        directory.delete();
    }

    /**
     * 递归删除目录中的目录或者文件 不删除目录本身
     * <p>
     * param directory
     */
    public static void cleanDirectory(File directory) {
        if (!directory.exists() || !directory.isDirectory()) {
            return;
        }
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            forceDelete(file);
        }
    }

    /**
     * 递归删除目录中的目录或者文件 删除目录本身
     * <p>
     * param file 目录或文件
     */
    public static void forceDelete(File file) {
        if (file.isDirectory()) {
            deleteDirectory(file);
        } else {
            boolean filePresent = file.exists();
            if (filePresent) {
                file.delete();
            }
        }
    }

    /**
     * 是否符号链接判断
     * <p>
     * param file
     * return
     * throws IOException
     */
    private static boolean isSymlink(File file) {
        try {
            if (file == null) {
                return false;
            }
            if (FilenameUtils.isSystemWindows()) {
                return false;
            }
            File fileInCanonicalDir = null;
            if (file.getParent() == null) {
                fileInCanonicalDir = file;
            } else {
                File canonicalDir = file.getParentFile().getCanonicalFile();
                fileInCanonicalDir = new File(canonicalDir, file.getName());
            }

            if (fileInCanonicalDir.getCanonicalFile().equals(
                    fileInCanonicalDir.getAbsoluteFile())) {
                return false;
            } else {
                return true;
            }
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * copy文件
     * <p>
     * param fromFile
     * param toFile
     */
    public static void copyFiles(String fromFile, String toFile) throws IOException {
        // 要复制的文件目录
        File[] currentFiles;
        File root = new File(fromFile);
        // 如同判断SD卡是否存在或者文件是否存在
        // 如果不存在则 return出去
        if (!root.exists()) {
            return;
        }
        // 如果存在则获取当前目录下的全部文件 填充数组
        currentFiles = root.listFiles();
        // 目标目录
        File targetDir = new File(toFile);
        // 创建目录
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        // 遍历要复制该目录下的全部文件
        for (int i = 0; i < currentFiles.length; i++) {
            if (currentFiles[i].isDirectory()) {
                // 如果当前项为子目录 进行递归
                File childdir = new File(toFile, currentFiles[i].getName());
                copyFiles(currentFiles[i].getPath() + "/",
                        childdir.getAbsolutePath());
            } else {
                // 如果当前项为文件则进行文件拷贝
                File mfile = new File(toFile, currentFiles[i].getName());
                copyFile(currentFiles[i].getPath(), mfile.getAbsolutePath());
            }
        }
    }

    public static void copyFiles(File fromFile, File toFile) throws IOException {
        String frompath = fromFile.getAbsolutePath();
        String topath = toFile.getAbsolutePath();
        copyFiles(frompath, topath);
    }

    public static void copyFile(String fromFile, String toFile) throws IOException {
        InputStream fosfrom = new FileInputStream(fromFile);
        OutputStream fosto = new FileOutputStream(toFile);
        byte bt[] = new byte[4096];
        int c;
        while ((c = fosfrom.read(bt)) > 0) {
            fosto.write(bt, 0, c);
        }
        fosfrom.close();
        fosto.close();
    }

    public static void copyFile(File fromFile, File toFile) throws IOException {
        String frompath = fromFile.getAbsolutePath();
        String topath = toFile.getAbsolutePath();
        copyFile(frompath, topath);
    }

    public static void save(String content, File tofile) throws IOException {
        if (TextUtils.isEmpty(content) || tofile == null) {
            return;
        }
        content = content.trim();
        if (tofile.exists()) {
            tofile.delete();
        }
        tofile.createNewFile();
        byte[] bs = content.getBytes();
        OutputStream fosto = new FileOutputStream(tofile);
        fosto.write(bs, 0, content.length());
        fosto.close();
    }

    public static void appendContent(String content, File tofile) throws IOException {
        if (TextUtils.isEmpty(content) || tofile == null) {
            return;
        }
        if (tofile.getParentFile() == null) {
            return;
        }
        content = content.trim();
        if (!tofile.exists()) {
            tofile.createNewFile();
        }
        byte[] bs = content.getBytes();
        OutputStream fosto = new FileOutputStream(tofile, true);
        fosto.write(bs, 0, content.length());
        fosto.close();
    }

    /**
     * 根据名称获取assets文件inputStream
     *
     * @param context  上下文
     * @param fileName 文件名
     * @return inputStream
     */
    public static InputStream getAssetsInputStream(Context context, String fileName) throws IOException {
        if (context == null || TextUtils.isEmpty(fileName)) {
            return null;
        }
        InputStream inputStream = context.getAssets().open(fileName);
        return inputStream;
    }

    /**
     * 获取文件(一般指文本文件)内容
     * <p>
     * param targetfile 目标文件
     * return 内容
     */
    public static String readContent(File targetfile) throws IOException {
        if (targetfile == null || !targetfile.exists()) {
            return "";
        }
        String result = "";
        FileInputStream fis = new FileInputStream(targetfile);
        int lenght = fis.available();
        byte[] buffer = new byte[lenght];
        fis.read(buffer);
        result = new String(buffer, "UTF-8");
        fis.close();
        return result;
    }

    /**
     * 读取流内容
     *
     * @param is 输出流
     * @return 输出内容
     */
    public static String readContent(InputStream is) throws IOException {
        if (is == null || is.available() <= 0) {
            return "";
        }
        String result = "";
        int lenght = is.available();
        byte[] buffer = new byte[lenght];
        is.read(buffer);
        result = new String(buffer, "UTF-8");
        is.close();
        return result;
    }

    /**
     * assets文件是否存在
     *
     * @param context  上下文
     * @param fileName 文件名
     * @return true-存在;false-不存在;
     */
    public static boolean isExsitsAssetsFile(Context context, String fileName) {
        if (context == null || TextUtils.isEmpty(fileName)) {
            return false;
        }
        AssetManager am = context.getAssets();
        try {
            boolean flag = false;
            String[] names = am.list("");
            for (String name : names) {
                if (name.equals(fileName.trim())) {
                    flag = true;
                    break;
                }
            }
            return flag;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 从assets取文本文件内容
     * <p>
     * param context
     * param fileName 文件名称
     * return
     */
    public static String readAssetsFileContent(Context context, String fileName) {
        try {
            if (context == null || TextUtils.isEmpty(fileName)) {
                return "";
            }
            if (!isExsitsAssetsFile(context, fileName)) {
                //如果不存在则返回
                return "";
            }
            String result = "";
            InputStream is = context.getAssets().open(fileName);
            int lenght = is.available();
            byte[] buffer = new byte[lenght];
            is.read(buffer);
            result = new String(buffer, "UTF-8");
            is.close();
            return result;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 保存Bitmap
     * <p>
     * param dir      目录
     * param filename 文件名
     * param bt       图片
     * return
     */
    public static File saveBitmap(File dir, String filename, Bitmap bt) throws IOException {
        File mfile = new File(dir, filename);
        if (mfile.exists()) {
            mfile.delete();
        }
        mfile.createNewFile();
        FileOutputStream out = new FileOutputStream(mfile);
        bt.compress(Bitmap.CompressFormat.PNG, 90, out);
        out.flush();
        out.close();
        return mfile;
    }

    /**
     * 获取文件或目录大小(单位为字节)
     * <p>
     * param fileOrDirPath 文件或目录
     * return
     */
    public static long getFileOrDirSize(File fileOrDirPath) {
        return getFolderSize(fileOrDirPath);
    }

    /**
     * 获取文件或目录大小(单位为字节)
     * <p>
     * param fileOrDirPath 文件或目录
     * return
     */
    public static long getFileOrDirSize(String fileOrDirPath) {
        File file = new File(fileOrDirPath);
        return getFolderSize(file);
    }

    private static long getFolderSize(File file) {
        long size = 0;
        File[] fileList = file.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                size = size + getFolderSize(fileList[i]);
            } else {
                size = size + fileList[i].length();
            }
        }
        return size;
    }
}
