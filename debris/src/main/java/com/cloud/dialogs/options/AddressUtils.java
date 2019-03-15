package com.cloud.dialogs.options;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.cloud.cache.DbCacheDao;
import com.cloud.cache.daos.OptionsItemDao;
import com.cloud.cache.greens.DBManager;
import com.cloud.dialogs.options.beans.OptionsItem;
import com.cloud.dialogs.options.enums.AddressLevel;
import com.cloud.dialogs.options.events.OnImportCompleteListener;
import com.cloud.dialogs.options.events.OnOptionsListener;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.storage.StorageUtils;
import com.cloud.objects.utils.JsonUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2018/11/1
 * Description:
 * Modifier:
 * ModifyContent:
 */
public class AddressUtils extends OptionsUtils implements OnOptionsListener {

    //显示区域级别(默认显示省-市-区)
    private AddressLevel addressLevel = AddressLevel.provinceCityRegion;

    @Override
    public AddressUtils builder(Context context, FragmentManager fragmentManager) {
        super.builder(context, fragmentManager);
        return this;
    }

    /**
     * 设置显示区域级别(默认显示省-市-区)
     *
     * @param addressLevel 显示区域级别(默认显示省-市-区){@link AddressLevel}
     */
    public AddressUtils setAddressLevel(AddressLevel addressLevel) {
        if (addressLevel == null) {
            return this;
        }
        this.addressLevel = addressLevel;
        return this;
    }

    private void addAddrItems() {
        if (addressLevel == AddressLevel.province) {
            super.addTabItem(getProvinceKey(), "请选择省", "请选择省");
        } else if (addressLevel == AddressLevel.provinceCity) {
            super.addTabItem(getProvinceKey(), "请选择省", "请选择省");
            super.addTabItem(getCityKey(), "请选择市", "请选择市");
        } else if (addressLevel == AddressLevel.provinceCityRegion) {
            super.addTabItem(getProvinceKey(), "请选择省", "请选择省");
            super.addTabItem(getCityKey(), "请选择市", "请选择市");
            super.addTabItem(getRegionKey(), "请选择区", "请选择区");
        }
    }

    /**
     * 获取省key
     *
     * @return
     */
    public String getProvinceKey() {
        return "province";
    }

    /**
     * 获取市key
     *
     * @return
     */
    public String getCityKey() {
        return "city";
    }

    /**
     * 获取区key
     *
     * @return
     */
    public String getRegionKey() {
        return "region";
    }

    /**
     * 显示选择项
     *
     * @param extras 扩展数据
     */
    public void show(Object... extras) {
        //根据区域级别添加省-市-区选项
        addAddrItems();
        super.setOnOptionsListener(this);
        super.setImportData(true);
        super.setDefParentId("0");
        super.show(extras);
    }

    @Override
    public void onImportLocalData(Context context, OnImportCompleteListener importCompleteCall) {
        String json = StorageUtils.readAssetsFileContent(context, "regions.rx");
        final List<OptionsItem> optionsItems = JsonUtils.parseArray(json, OptionsItem.class);
        if (ObjectJudge.isNullOrEmpty(optionsItems)) {
            return;
        }
        DbCacheDao dbCacheDao = new DbCacheDao();
        OptionsItemDao optionsItemDao = dbCacheDao.getOptionsItemDao();
        if (optionsItemDao != null) {
            optionsItemDao.insertOrReplaceInTx(optionsItems);
            DBManager.getInstance().close();
        }
    }

    @Override
    public List<OptionsItem> getOptionsItems(String targetId, final String parentId) {
        final List<OptionsItem> list = new ArrayList<OptionsItem>();
        DbCacheDao dbCacheDao = new DbCacheDao();
        OptionsItemDao optionsItemDao = dbCacheDao.getOptionsItemDao();
        if (optionsItemDao != null) {
            QueryBuilder<OptionsItem> builder = optionsItemDao.queryBuilder();
            builder.where(OptionsItemDao.Properties.ParentId.eq(parentId));
            list.addAll(builder.build().list());
            DBManager.getInstance().close();
        }
        return list;
    }
}
