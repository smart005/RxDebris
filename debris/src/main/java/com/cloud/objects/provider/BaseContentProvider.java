package com.cloud.objects.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.cloud.objects.beans.UriMatcherItem;
import com.cloud.objects.utils.JsonUtils;

import java.util.List;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2017/7/16
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public abstract class BaseContentProvider extends ContentProvider {

    protected UriMatcher uriMatcher = new UriMatcher();

    @Override
    public boolean onCreate() {
        return true;
    }

    protected String onGetContent(UriMatcherItem uriMatcherItem) {
        return "";
    }

    protected String onGetContent(UriMatcherItem uriMatcherItem, List<ContentProviderQueryWhereItem> whereItems) {
        return "";
    }

    protected String onGetContent(UriMatcherItem uriMatcherItem, ContentProviderQueryWhereItem whereItem) {
        return "";
    }

    protected int onGetContent(UriMatcherItem uriMatcherItem, String content) {
        return 0;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        UriMatcherItem matcherItem = uriMatcher.match(uri);
        return onGetContent(matcherItem);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        UriMatcherItem matcherItem = uriMatcher.match(uri);
        List<ContentProviderQueryWhereItem> whereItems = null;
        if (values != null &&
                values.containsKey(BaseContentProviderUtils.QUERY_CONDITION_DATA_KEY)) {
            String where = values.getAsString(BaseContentProviderUtils.QUERY_CONDITION_DATA_KEY);
            whereItems = JsonUtils.parseArray(where, ContentProviderQueryWhereItem.class);
        }
        if (whereItems == null || whereItems.isEmpty()) {
            return Uri.EMPTY;
        }
        if (whereItems.size() > 1) {
            return Uri.parse(onGetContent(matcherItem, whereItems));
        } else {
            ContentProviderQueryWhereItem queryWhereItem = whereItems.get(0);
            if (queryWhereItem.isExtrasQuery()) {
                return Uri.parse(onGetContent(matcherItem, queryWhereItem));
            } else {
                return Uri.parse(onGetContent(matcherItem, whereItems));
            }
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String content, String[] selectionArgs) {
        UriMatcherItem matcherItem = uriMatcher.match(uri);
        return onGetContent(matcherItem, content);
    }
}
