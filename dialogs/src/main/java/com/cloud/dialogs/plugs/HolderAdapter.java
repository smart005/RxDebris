package com.cloud.dialogs.plugs;

import android.widget.BaseAdapter;

public interface HolderAdapter extends Holder {

    public void setAdapter(BaseAdapter adapter);

    public void setOnItemClickListener(OnHolderListener listener);
}
