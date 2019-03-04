package com.cloud.objects.provider;

import android.net.Uri;
import android.text.TextUtils;

import com.cloud.objects.beans.UriMatcherItem;

import java.util.HashMap;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/7/16
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class UriMatcher {

    private HashMap<String, UriMatcherItem> datalist = new HashMap<String, UriMatcherItem>();

    public void addUri(String uri, String action, int code) {
        if (TextUtils.isEmpty(uri) ||
                TextUtils.isEmpty(action)) {
            return;
        }
        UriMatcherItem item = new UriMatcherItem();
        item.setUri(uri);
        item.setAction(action);
        item.setCode(code);
        datalist.put(action, item);
    }

    public UriMatcherItem match(Uri uri) {
        if (uri == null) {
            return new UriMatcherItem();
        }
        String action = uri.getLastPathSegment();
        if (TextUtils.isEmpty(action)) {
            return new UriMatcherItem();
        }
        if (datalist.containsKey(action)) {
            UriMatcherItem item = datalist.get(action);
            if (item == null) {
                return new UriMatcherItem();
            } else {
                return item;
            }
        } else {
            return new UriMatcherItem();
        }
    }
}
