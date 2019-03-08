package com.cloud.images2.figureset;

import android.net.Uri;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2016/7/21
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
interface OnPictureSelectChangedListener {
    void onPictureSelectChanged(Uri imgUri, String fileName, int position);
}
