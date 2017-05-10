package com.example.graduationproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.graduationproject.R;
import com.example.graduationproject.entity.Gateway;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuji on 2017/5/9.
 */

public class GatewayAdapter extends BaseAdapter{

    private Context context;
    private List<Gateway> datas = new ArrayList<>();

    public GatewayAdapter(Context context,List<Gateway> datas){
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.adapter_gateway, null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.ip = (TextView)convertView.findViewById(R.id.tv_ip);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(datas.get(position).getName());
        viewHolder.ip.setText(datas.get(position).getIp());

        return convertView;
    }

    class ViewHolder {
        TextView name;
        TextView ip;
    }
}
