package com.dev.partialrefreshdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DataAdapter.PartialRefreshListener {

    private ListView mLvData;
    private List<ItemData> mDatas;
    private DataAdapter mAdapter;

    private int currentItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        bindData();
    }

    private void bindData() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            ItemData data = new ItemData();
            data.setContent("第" + i + "条数据");
            mDatas.add(data);
        }
        mAdapter = new DataAdapter(this, mDatas);
        mLvData.setAdapter(mAdapter);
    }

    private void init() {
        mLvData = (ListView) findViewById(R.id.lv_data);
        mLvData.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE://处于闲置状态
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://处于触摸滑动状态
                        currentItem = view.getFirstVisiblePosition();
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mAdapter == null) return;
                //处于下滑
                if (currentItem > firstVisibleItem) {
                    //这里需要提供要刷新的数据
                    ItemData data = new ItemData();
                    data.setContent("第" + (firstVisibleItem) + "条数据被刷新");
                    //因为有可能滑动速度太快导致更新不是很及时、所以需要提前去刷新(firstVisibleItem + n)
                    mAdapter.updateItemData(firstVisibleItem, data, MainActivity.this);
                    currentItem = firstVisibleItem;
                } else {
                    //这里需要提供要刷新的数据
                    ItemData data = new ItemData();
                    data.setContent("第" + (firstVisibleItem + visibleItemCount) + "条数据被刷新");
                    //因为有可能滑动速度太快导致更新不是很及时、所以需要提前去刷新
                    mAdapter.updateItemData(firstVisibleItem + visibleItemCount, data, MainActivity.this);
                    currentItem = firstVisibleItem;
                }
            }
        });
    }

    /**
     * 实现更新Item视图方法
     *
     * @param position
     */
    @Override
    public void updateItemView(int position) {
        if (mLvData == null) return;
        View view = mLvData.getChildAt(position);
        if (view != null) {
            TextView tvContent = (TextView) view.findViewById(R.id.tv_content);
            tvContent.setText(mDatas.get(position).getContent());
        }
    }
}
