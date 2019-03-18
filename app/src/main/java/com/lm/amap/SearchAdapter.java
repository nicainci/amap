package com.lm.amap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author LM
 * @Create 2019/3/6
 * @Description SearchAdapter
 */
public class SearchAdapter extends BaseAdapter implements Filterable {

    private List<Tip> list;
    private Filter filter;

    public SearchAdapter(Context context) {
        this.list = new ArrayList<>();
        this.filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                InputtipsQuery inputtipsQuery = new InputtipsQuery(constraint.toString(), null);
                Inputtips inputTips = new Inputtips(context, inputtipsQuery);
                inputTips.setInputtipsListener((list, resultCode) -> {
                    if (resultCode == 1000) {
                        setList(list);
                    } else {
                        Toast.makeText(context, "搜索结果出错 resultCode=" + resultCode, Toast.LENGTH_SHORT).show();
                    }
                });
                inputTips.requestInputtipsAsyn();
                return null;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

            }
        };
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Tip getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder(parent);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Tip tip = getItem(position);
        viewHolder.tvName.setText(tip.getName());
        viewHolder.tvAddress.setText(tip.getDistrict());

        return viewHolder.itemView;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public void setList(List<Tip> newList) {
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }

    class ViewHolder {
        View itemView;
        TextView tvName;
        TextView tvAddress;

        ViewHolder(ViewGroup parent) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
            tvName = itemView.findViewById(R.id.tv_name);
            tvAddress = itemView.findViewById(R.id.tv_address);
            itemView.setTag(this);
        }
    }
}
