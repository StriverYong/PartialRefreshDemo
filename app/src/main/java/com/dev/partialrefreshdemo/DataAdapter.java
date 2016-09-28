package com.dev.partialrefreshdemo;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by StriverYong on 2016/9/28.
 */

public class DataAdapter extends BaseAdapter {

    private List<ItemData> mList;
    private LayoutInflater mInflater;

    private PartialRefreshListener mListener;

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            if (mListener != null && hasMessages(msg.what)) {
                mListener.updateItemView(msg.what);
            }
        }
    };

    public DataAdapter(Context context, List<ItemData> list) {
        mInflater = LayoutInflater.from(context);
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_data, null);
            holder = new ViewHolder();
//            //因为item布局的的根节点就是TextView所以这里可以直接转，显然实际开发不会这样做
//            viewHolder.textView = (TextView) convertView;
            holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvContent.setText(mList.get(position).getContent());
        return convertView;
    }

    class ViewHolder {
        TextView tvContent;
    }

    /**
     * 更新指定条目的数据
     *
     * @param position
     * @param item
     */
    public void updateItemData(int position, ItemData item, PartialRefreshListener listener) {
        if (listener != null && position >= 0 && position < mList.size()) {
            this.mListener = listener;
            //替换指定条目数据
            mList.set(position, item);
            //因为刷新数据有可能为耗时操作，所以用handler做界面刷新
            mHandler.sendEmptyMessage(position);
        }
    }

    public interface PartialRefreshListener {
        void updateItemView(int position);
    }
}
