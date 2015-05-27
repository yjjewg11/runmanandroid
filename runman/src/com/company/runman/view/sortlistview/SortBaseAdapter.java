package com.company.runman.view.sortlistview;

import android.content.Context;
import android.view.View;
import android.widget.SectionIndexer;
import com.company.runman.activity.Adapter.BaseListAdapter;
import com.company.runman.datacenter.model.MonitorStationEntity;

import java.util.List;

public class SortBaseAdapter extends BaseListAdapter<MonitorStationEntity> implements SectionIndexer, View.OnClickListener {

    private static final String TAG = "SortAdapter";
    protected Context context;

    public SortBaseAdapter(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<MonitorStationEntity> list) {
        mList = list;
        notifyDataSetChanged();
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的char ascii值
     */
    public int getSectionForPosition(int position) {
        return mList.get(position).getSortLetters().charAt(0);
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = mList.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }


    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public void onClick(View v) {

    }
}