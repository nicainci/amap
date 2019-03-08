package com.lm.amap;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.AsyncDifferConfig;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * @Author WWC
 * @Create 2019/3/6
 * @Description 描述
 * Copyright(c) 2017, Zhejiang Yunbo Technology Co.,Ltd. All rights reserved
 */
public class SearchAdapter extends ListAdapter {
    protected SearchAdapter(@NonNull AsyncDifferConfig config) {
        super(config);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }
}
