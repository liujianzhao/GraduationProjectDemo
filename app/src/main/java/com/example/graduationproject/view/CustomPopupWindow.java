package com.example.graduationproject.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.graduationproject.R;

import java.util.List;

/**
 * Created by liuji on 2017/5/11.
 */

public class CustomPopupWindow extends LinearLayout {

    private Context context;
    private LayoutInflater inflater;
    private TextView tv_channel, tv_node;
    private List<String> nodeDatas,channelDatas;
    private OnPopupWindowClose onPopupWindowClose;

    public interface OnPopupWindowClose{
        public void loadData();
    }

    public CustomPopupWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        inflater = LayoutInflater.from(context);
        initView();
    }

    public CustomPopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        inflater = LayoutInflater.from(context);
        initView();
    }

    public CustomPopupWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        inflater = LayoutInflater.from(context);
        initView();
    }

    public CustomPopupWindow(Context context) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
        initView();
    }

    public String getNodeText(){
        return tv_node.getText().toString();
    }

    public String getChannelText(){
        return tv_channel.getText().toString();
    }

    public void setDatas(List<String> nodeDatas,List<String> channelDatas){
        this.nodeDatas = nodeDatas;
        this.channelDatas = channelDatas;
    }

    public void setOnClickListener(OnPopupWindowClose onPopupWindowClose){
        this.onPopupWindowClose = onPopupWindowClose;
        tv_node.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nodeDatas != null){
                    showPopupWindow(nodeDatas,tv_node);
                }
            }
        });
        tv_channel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(channelDatas != null){
                    showPopupWindow(channelDatas,tv_channel);
                }
            }
        });
    }

    private void initView() {
        View v = inflater.inflate(R.layout.layout_custom_popupwindow,null);
        tv_node = (TextView)v.findViewById(R.id.tv_node);
        tv_channel = (TextView)v.findViewById(R.id.tv_channel);
        addView(v);
    }

    private void showPopupWindow(final List<String> list, final TextView view) {
        final MyPopupWindow mpw = new MyPopupWindow(context, view);
        PopupWinAdapter adapter = new PopupWinAdapter(context, list,
                PopupWinAdapter.STRINGDATA);
        mpw.getListView().setAdapter(adapter);
        mpw.getPopWin().showAsDropDown(view);
        mpw.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                view.setText(list.get(position));
                mpw.getPopWin().dismiss();
                if(!tv_channel.getText().toString().equals("") && !tv_node.getText().toString().equals("")) {
                    onPopupWindowClose.loadData();
                }
            }
        });
    }

   private class MyPopupWindow {

        private ListView listView;
        private PopupWindow popWin;

        public MyPopupWindow(Context context, TextView view) {
            initView(context, view);
        }

        @SuppressWarnings("deprecation")
        private void initView(Context context, TextView view) {
            View layout = LayoutInflater.from(context).inflate(
                    R.layout.item_custom_popupwindow, null);
            listView = (ListView) layout.findViewById(R.id.listview);

            popWin = new PopupWindow(layout, view.getMeasuredWidth(), 700, true);
            popWin.setOutsideTouchable(true);
            popWin.setBackgroundDrawable(new BitmapDrawable());
        }

        public ListView getListView() {
            return listView;
        }

        public PopupWindow getPopWin() {
            return popWin;
        }

    }

    private class PopupWinAdapter extends BaseAdapter {

        public static final int STRINGDATA = 0;

        private Context context;
        private int type;
        private List<String> datas;

        public PopupWinAdapter(Context context, List<?> datas,
                               int type) {
            this.context = context;
            this.type = type;
            switch(type){
                case 0:
                    this.datas = (List<String>) datas;
                    break;
            }
        }

        @Override
        public int getCount() {
            int size = 0;
            switch(type){
                case 0:
                    size = datas.size();
                    break;
            }
            return size;
        }

        @Override
        public Object getItem(int position) {
            Object obj = null;
            switch(type){
                case 0:
                    obj = datas.get(position);
                    break;
            }
            return obj;
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
                        R.layout.adapter_popupwin, null);
                viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            switch(type){
                case 0:
                    viewHolder.name.setText(datas.get(position));
                    break;
            }
            return convertView;
        }

        class ViewHolder {
            TextView name;
        }

    }
}
