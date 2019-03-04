package com.cloud.dialogs.plugs;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.cloud.dialogs.R;

public class GridHolder implements HolderAdapter, AdapterView.OnItemClickListener {

    private final int columnNumber;

    private int backgroundResource;

    private GridView gridView;
    private ViewGroup headerContainer;
    private ViewGroup footerContainer;
    private OnHolderListener listener;
    private View.OnKeyListener keyListener;
    private View headerView;
    private View footerView;

    public GridHolder(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    @Override
    public void addHeader(View view) {
        if (view == null) {
            return;
        }
        headerContainer.addView(view);
        headerView = view;
    }

    @Override
    public void addFooter(View view) {
        if (view == null) {
            return;
        }
        footerContainer.addView(view);
        footerView = view;
    }

    @Override
    public void setAdapter(BaseAdapter adapter) {
        gridView.setAdapter(adapter);
    }

    @Override
    public void setBackgroundResource(int colorResource) {
        this.backgroundResource = colorResource;
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.dialog_plugs_grid, parent, false);
        View outMostView = view.findViewById(R.id.dialogplus_outmost_container);
        outMostView.setBackgroundResource(backgroundResource);
        gridView = (GridView) view.findViewById(R.id.dialogplus_list);
        gridView.setNumColumns(columnNumber);
        gridView.setOnItemClickListener(this);
        gridView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyListener == null) {
                    throw new NullPointerException("keyListener should not be null");
                }
                return keyListener.onKey(v, keyCode, event);
            }
        });
        headerContainer = (ViewGroup) view.findViewById(R.id.dialogplus_header_container);
        footerContainer = (ViewGroup) view.findViewById(R.id.dialogplus_footer_container);
        return view;
    }

    @Override
    public void setOnItemClickListener(OnHolderListener listener) {
        this.listener = listener;
    }

    @Override
    public void setOnKeyListener(View.OnKeyListener keyListener) {
        this.keyListener = keyListener;
    }

    @Override
    public View getInflatedView() {
        return gridView;
    }

    @Override
    public View getHeader() {
        return headerView;
    }

    @Override
    public View getFooter() {
        return footerView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (listener == null) {
            return;
        }
        listener.onItemClick(parent.getItemAtPosition(position), view, position);
    }
}
