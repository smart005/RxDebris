package com.cloud.objects.storage;

import android.text.TextUtils;

import com.cloud.objects.ObjectJudge;
import com.cloud.objects.beans.ElementEntry;
import com.cloud.objects.config.Recycling;
import com.cloud.objects.events.OnRecyclingListener;
import com.cloud.objects.logs.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/4/4
 * Description:目录管理;
 * 存储方式:[images->[forum->[video,temp],user->[info,vip],comments]]
 * 取方式:{@link StorageUtils}.root + images->forum->video,此时可以传
 * 注:
 * 1.目录结构确保不重复,若不同的目录下存在名字相同的两个子目录则取前一个优先匹配到的一个;
 * 2.最后一
 * Modifier:
 * ModifyContent:
 */
public class DirectoryUtils implements OnRecyclingListener {

    private static DirectoryUtils directoryUtils;
    private ElementEntry elementEntry = new ElementEntry();
    private HashMap<String, File> directorieMap = new HashMap<String, File>();

    private DirectoryUtils() {
        //init
        Recycling.getInstance().addRecyclingListener(this);
    }

    public static DirectoryUtils getInstance() {
        return directoryUtils == null ? directoryUtils = new DirectoryUtils() : directoryUtils;
    }

    @Override
    public void recycling() {
        directorieMap.clear();
        directoryUtils = null;
    }

    /**
     * 添加目录
     *
     * @param directory 目录
     * @return ElementEntry
     */
    public DirectoryUtils addDirectory(String directory) {
        elementEntry = elementEntry.addElement(directory);
        return this;
    }

    /**
     * 添加子目录
     *
     * @param directory 子目录
     * @return ElementEntry
     */
    public DirectoryUtils addChildDirectory(String directory) {
        elementEntry = elementEntry.next(directory);
        return this;
    }

    /**
     * 根据层级数返回上级目录并添加目录(directory)
     *
     * @param prevSeriesCount 层级数
     * @param directory       要添加的目录
     * @return ElementEntry
     */
    public DirectoryUtils prevDirectory(int prevSeriesCount, String directory) {
        elementEntry = elementEntry.prev(prevSeriesCount, directory);
        return this;
    }

    /**
     * 返回上一级目录并添加目录(directory)
     *
     * @param directory 要添加的目录
     * @return ElementEntry
     */
    public DirectoryUtils prevDirectory(String directory) {
        elementEntry = elementEntry.prev(directory);
        return this;
    }

    /**
     * 构建目录
     * (根目录为{@link StorageUtils}.root)
     */
    public void buildDirectories() {
        File rootDir = StorageUtils.getRootDir();
        LinkedList<String> allElementPaths = elementEntry.getAllElementPaths();
        for (String elementPath : allElementPaths) {
            File dir = new File(rootDir, elementPath);
            if (!dir.exists()) {
                boolean mkdirs = dir.mkdirs();
                if (!mkdirs) {
                    Logger.info("create directory fail");
                }
            }
        }
    }

    /**
     * 获取目录
     * (根目录为{@link StorageUtils}.root)
     *
     * @param directoryName 目录名称
     * @return 目录
     */
    public File getDirectory(String directoryName) {
        File rootDir = StorageUtils.getRootDir();
        if (TextUtils.isEmpty(directoryName)) {
            return rootDir;
        }
        if (directorieMap.containsKey(directoryName)) {
            File dir = directorieMap.get(directoryName);
            if (dir != null) {
                if (dir.exists()) {
                    return dir;
                }
            }
        }
        if (TextUtils.equals(rootDir.getName(), directoryName)) {
            return rootDir;
        }
        File directory = getMatchDirectory(rootDir, directoryName);
        if (directory == null) {
            //若无匹配则返回根目录
            return rootDir;
        }
        directorieMap.put(directory.getName(), directory);
        return directory;
    }

    private File getMatchDirectory(File dir, String directoryName) {
        File[] files = dir.listFiles();
        if (ObjectJudge.isNullOrEmpty(files)) {
            return null;
        }
        for (File file : files) {
            if (TextUtils.equals(file.getName(), directoryName)) {
                return file;
            } else {
                File directory = getMatchDirectory(file, directoryName);
                if (directory != null) {
                    return directory;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return elementEntry.toString();
    }
}
