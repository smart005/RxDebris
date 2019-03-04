package com.cloud.cache;

import android.text.TextUtils;
import android.util.LruCache;

import com.cloud.objects.events.OnRecyclingListener;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/1/14
 * Description:内存缓存
 * Modifier:
 * ModifyContent:
 */
public class MemoryCache implements OnRecyclingListener {

    private static MemoryCache memoryCache = null;

    private LruCache<String, Object> lruCacheMap = null;
    private HashMap<String, Object> softMap = new HashMap<String, Object>();
    private SoftReference<HashMap<String, Object>> softReference = new SoftReference<HashMap<String, Object>>(softMap);

    private String ignoreTag = "ignore";
    private boolean isReLoad = true;

    private MemoryCache() {
        int maxMemory = (int) (Runtime.getRuntime().freeMemory() / 1024);
        int cacheSize = maxMemory / 32;
        if (cacheSize > 2000) {
            lruCacheMap = new LruCache<>(cacheSize);
        } else {
            lruCacheMap = new LruCache<>(2000);
        }
    }

    public static MemoryCache getInstance() {
        return memoryCache == null ? memoryCache = new MemoryCache() : memoryCache;
    }

    @Override
    public void recycling() {
        memoryCache = null;
        //清除可忽略的缓存
        if (lruCacheMap != null) {
            Set<String> keys = new HashSet<String>();
            Map<String, Object> snapshot = lruCacheMap.snapshot();
            for (Map.Entry<String, Object> entry : snapshot.entrySet()) {
                String key = entry.getKey();
                if (key == null || key.contains(ignoreTag)) {
                    keys.add(entry.getKey());
                }
            }
            for (String key : keys) {
                lruCacheMap.remove(key);
            }
        }
        isReLoad = true;
    }

    /**
     * 设置缓存
     *
     * @param key     缓存键
     * @param content 缓存内容
     */
    public void set(String key, Object content) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        lruCacheMap.put(key, content);
    }

    /**
     * 获取缓存
     *
     * @param key 缓存键
     * @param <T> 返回值类型
     * @return 返回值
     */
    public <T> T get(String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        Object o = lruCacheMap.get(key);
        if (o == null) {
            o = lruCacheMap.get(ignoreTag + key);
            return o == null ? null : (T) o;
        } else {
            return (T) o;
        }
    }

    /**
     * 从缓存中移除对象
     *
     * @param key 缓存键
     * @param <T> 返回类型
     * @return 返回当前被移除的对象
     */
    public <T> T remove(String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        Object remove = lruCacheMap.remove(key);
        if (remove == null) {
            remove = lruCacheMap.remove(ignoreTag + key);
            return remove == null ? null : (T) remove;
        } else {
            return (T) remove;
        }
    }

    /**
     * 设置弱缓存,在被gc回收之前,调用recycling()时会被回收
     *
     * @param key     缓存键
     * @param content 缓存内容
     */
    public void setWeakCache(String key, Object content) {
        set(ignoreTag + key, content);
    }

    /**
     * 设置软缓存,在内存极度底下时被清理
     *
     * @param key     缓存键
     * @param content 缓存内容
     */
    public void setSoftCache(String key, Object content) {
        if (TextUtils.isEmpty(key) || content == null) {
            return;
        }
        HashMap<String, Object> map = softReference.get();
        if (map == null) {
            softReference = new SoftReference<HashMap<String, Object>>(softMap);
            map = softReference.get();
            //再次检测
            if (map == null) {
                return;
            }
        }
        map.put(key, content);
    }

    /**
     * 获取软缓存,在内存极度底下时被清理
     *
     * @param key 缓存键
     */
    public <T> T getSoftCache(String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        HashMap<String, Object> map = softReference.get();
        if (map == null || !map.containsKey(key)) {
            return null;
        }
        Object o = map.get(key);
        if (o == null) {
            return null;
        } else {
            return (T) o;
        }
    }
}
