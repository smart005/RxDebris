package com.cloud.cache;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/4/4
 * Description:路径缓存信息数据项
 * Modifier:
 * ModifyContent:
 */
@Entity(nameInDb = "rx_path_cache_data_item")
public class PathCacheInfoItem {
    /**
     * url
     */
    @Id()
    @Property(nameInDb = "url")
    @Index(unique = true)
    private String url = "";
    /**
     * 本地路径
     */
    @Property(nameInDb = "path")
    private String path = "";
    /**
     * 目标路径
     */
    @Property(nameInDb = "targetPath")
    private String targetPath = "";
    /**
     * 名字
     */
    @Property(nameInDb = "name")
    private String name = "";

    @Generated(hash = 619751143)
    public PathCacheInfoItem(String url, String path, String targetPath,
            String name) {
        this.url = url;
        this.path = path;
        this.targetPath = targetPath;
        this.name = name;
    }

    @Generated(hash = 1274660314)
    public PathCacheInfoItem() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
